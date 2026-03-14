package mod1;

public class AllErrors {
    public static class sendHTTPError extends Exception {
        public sendHTTPError(String msg) {
            super(msg);
            System.out.println("problem with the getHTTP method in Utils");
        }
    }
}