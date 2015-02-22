package ini;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Zdenec    .
 * User: Zdenec
 * WWW BASKET TREBIC
 */
public class ErrorLog {
    private static TeePrintStream ts;

    public static void init(String[] args, String prefix) {
        for (String arg : args) {
            if ("log".equalsIgnoreCase(arg)) {
                enableLog(prefix);
                return;
            }
        }

    }

    private static void enableLog(String prefix) {
        try {
            ts = new TeePrintStream(System.err, prefix + "-error.log", true);
            System.setErr(ts);
            System.err.println("init error log");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TeePrintStream extends PrintStream {
        protected PrintStream parent;

        protected String fileName;


        /**
         * Construct a TeePrintStream given an existing PrintStream, an opened
         * OutputStream, and a boolean to control auto-flush. This is the main
         * constructor, to which others delegate via "this".
         */
        public TeePrintStream(PrintStream orig, OutputStream os, boolean flush)
                throws IOException {
            super(os, flush);
            fileName = "(opened Stream)";
            parent = orig;
        }

        /**
         * Construct a TeePrintStream given an existing PrintStream and an opened
         * OutputStream.
         */
        public TeePrintStream(PrintStream orig, OutputStream os) throws IOException {
            this(orig, os, true);
        }

        /*
         * Construct a TeePrintStream given an existing Stream and a filename.
         */
        public TeePrintStream(PrintStream os, String fn) throws IOException {
            this(os, fn, true);
        }

        /*
         * Construct a TeePrintStream given an existing Stream, a filename, and a
         * boolean to control the flush operation.
         */
        public TeePrintStream(PrintStream orig, String fn, boolean flush)
                throws IOException {
            this(orig, new FileOutputStream(fn), flush);
        }

        /**
         * Return true if either stream has an error.
         */
        public boolean checkError() {
            return parent.checkError() || super.checkError();
        }

        /**
         * override write(). This is the actual "tee" operation.
         */
        public void write(int x) {
            parent.write(x); // "write once;
            super.write(x); // write somewhere else."
        }

        /**
         * override write(). This is the actual "tee" operation.
         */
        public void write(byte[] x, int o, int l) {
            parent.write(x, o, l); // "write once;
            super.write(x, o, l); // write somewhere else."
        }

        /**
         * Close both streams.
         */
        public void close() {
            parent.close();
            super.close();
        }

        /**
         * Flush both streams.
         */
        public void flush() {
            parent.flush();
            super.flush();
        }
    }
}
