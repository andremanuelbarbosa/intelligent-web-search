/*
 * WebSphinx web-crawling toolkit
 *
 * Copyright (c) 1998-2002 Carnegie Mellon University.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CARNEGIE MELLON UNIVERSITY ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL CARNEGIE MELLON UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package websphinx.workbench;

import websphinx.*;
import java.io.File;
import java.io.IOException;

public class ConcatAction implements Action, CrawlListener {
    String filename;
    boolean useBrowser;
    String prolog, header, footer, divider, epilog;

    transient File file;
    transient Concatenator concat;

    public ConcatAction (String filename, boolean useBrowser) {
        this.filename = filename;
        this.useBrowser = useBrowser;
    }

    public ConcatAction (String filename, boolean useBrowser,
                         String prolog, String header, String footer, 
                         String divider, String epilog) {
        this (filename, useBrowser);
        this.prolog = prolog;
        this.header = header;
        this.footer = footer;
        this.divider = divider;
        this.epilog = epilog;
    }
    
    public boolean equals (Object object) {
        if (! (object instanceof ConcatAction))
            return false;
        ConcatAction a = (ConcatAction)object;
        return same (a.filename, filename) && a.useBrowser == useBrowser;
    }
    
    private boolean same (String s1, String s2) {
        if (s1 == null || s2 == null)
            return s1 == s2;
        else
            return s1.equals (s2);
    }

    public String getFilename () {
        return filename;
    }

    public boolean getUseBrowser () {
        return useBrowser;
    }

    private transient boolean oldSync;

    public void connected (Crawler crawler) {
        oldSync = crawler.getSynchronous ();
        crawler.setSynchronous (true);
        crawler.addCrawlListener (this);
    }

    public void disconnected (Crawler crawler) {
        crawler.setSynchronous (oldSync);
        crawler.removeCrawlListener (this);
    }
   

    private void showit () {
      Browser browser = Context.getBrowser();
      if (browser != null)
        browser.show (file);
    }

    public synchronized void visit (Page page) {
        try {
            concat.writePage (page);
        } catch (IOException e) {
            throw new RuntimeException (e.toString());
        }
    }

    /**
     * Notify that the crawler started.
     */
    public void started (CrawlEvent event){
        if (concat == null) {
            try {
                file = (filename != null)
                  ? new File (filename)
                  : Access.getAccess ().makeTemporaryFile ("concat", ".html");
                concat = new Concatenator (file.toString());
                
                if (prolog != null)
                    concat.setProlog (prolog);
                if (header != null)
                    concat.setPageHeader (header);
                if (footer != null)
                    concat.setPageFooter (footer);
                if (divider != null)
                    concat.setDivider (divider);
                if (epilog != null)
                    concat.setEpilog (epilog);
            } catch (IOException e) {
                System.err.println (e); // FIX: use GUI when available
            }
        }
    }

    /**
     * Notify that the crawler ran out of links to crawl
     */
    public void stopped (CrawlEvent event){
        if (concat != null) {
            try {
                concat.close ();
                concat = null;
                if (useBrowser)
                    showit ();
            } catch (IOException e) {
                System.err.println (e); // FIX: use GUI when available
            }
        }
    }

    /**
     * Notify that the crawler's state was cleared.
     */
    public void cleared (CrawlEvent event){
        try {
            if (concat != null) {
                concat.close ();
                concat = null;
                if (useBrowser)
                    showit ();
            }
        } catch (IOException e) {
            System.err.println (e); // FIX: use GUI when available
        }
    }

    /**
     * Notify that the crawler timed out.
     */
    public void timedOut (CrawlEvent event){
        try {
            if (concat != null) {
                concat.close ();
                concat = null;
                if (useBrowser)
                    showit ();
            }
        } catch (IOException e) {
            System.err.println (e); // FIX: use GUI when available
        }
    }

    /**
     * Notify that the crawler is paused.
     */
    public void paused (CrawlEvent event){
        try {
            if (concat != null) {
                concat.rewrite ();
                if (useBrowser)
                    showit ();
            }
        } catch (IOException e) {
            System.err.println (e); // FIX: use GUI when available
        }
    }

}

