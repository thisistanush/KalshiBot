package java;

public class AllErrors {
    public static class getHTTPSError extends Exception {
        public getHTTPSError(String msg) {
            super(msg);
            System.out.println("problem with teh getHTTPS method in Utils");
        }
    }

}
