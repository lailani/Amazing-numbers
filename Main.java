package numbers;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to Amazing Numbers!\n\n" +
                "Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.");
        start();
    }

    public static void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a request: ");
        String inputString = scanner.nextLine().toLowerCase(Locale.ROOT);
        String[] input = inputString.split(" ");


        if (input.length == 1) {
            long n = Long.parseLong(input[0]);
            startForOneNumber(n);
        } else if (input.length == 2) {
            long n1 = Long.parseLong(input[0]);
            long n2 = Long.parseLong(input[1]);
            startForSequence(n1, n2);
//        } else if (input.length == 3) {
//            long n1 = Long.parseLong(input[0]);
//            long n2 = Long.parseLong(input[1]);
//            String property = input[2].toLowerCase(Locale.ROOT);
//            startForFindNumbersWithOneProperty(n1, n2, property);
//        }
//        else if (input.length == 4) {
//            long n1 = Long.parseLong(input[0]);
//            long n2 = Long.parseLong(input[1]);
//            String property1 = input[2].toLowerCase(Locale.ROOT);
//            String property2 = input[3].toLowerCase(Locale.ROOT);
//            startForFindNumbersWithTwoProperties(n1, n2, property1, property2);
        } else if (input.length >= 2) {
            long n1 = Long.parseLong(input[0]);
            long n2 = Long.parseLong(input[1]);
            String[] properties = new String[input.length - 2];
            System.arraycopy(input, 2, properties, 0, input.length - 2);
            startForFindNumbersWithManyProperties(n1, n2, properties);
        }

    }


    public static void startForOneNumber(long n) {
        if (n == 0) {
            System.out.println("Goodbye!");
            System.exit(0);
        } else if (!checkNatural(n)) {
            System.out.println("The first parameter should be a natural number or zero.");
            start();
        } else {
            printProperties(n);
            start();
        }
    }

    public static void startForSequence(long n1, long n2) {
        if (n1 < 0) {
            System.out.println("The first parameter should be a natural number or zero.");
            start();
        } else if (!checkNatural(n2)) {
            System.out.println("The second parameter should be a natural number.");
            start();
        } else {
            for (long i = n1; i < n1 + n2; i++) {
                printPropertiesInLine(i);
            }
            start();
        }
    }

    public static void startForFindNumbersWithOneProperty(long n1, long n2, String property) {
        long count = 0;
        if (!checkInputProperty(property)) {
            System.out.printf("The property [%s] is wrong.\n" +
                    "Available properties: [BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, " +
                    "SQUARE, SUNNY, EVEN, ODD, JUMPING, HAPPY, SAD]\n", property);
        } else {
            while (count != n2) {
                if (checkStringProperty(n1, property)) {
                    printPropertiesInLine(n1);
                    count++;
                }
                n1++;
            }
        }
        start();
    }

    public static void startForFindNumbersWithTwoProperties(long n1, long n2, String property1, String property2) {

        if (!checkInputProperty(property1) && !checkInputProperty(property2)) {
            System.out.printf("The properties [%s, %s] are wrong.\n" +
                    "Available properties: " +
                    "[BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, EVEN, ODD]\n", property1, property2);
        } else if (!checkInputProperty(property1)) {
            System.out.printf("The property [%s] is wrong.\n" +
                    "Available properties: " +
                    "[BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, EVEN, ODD]\n", property1);
        } else if (!checkInputProperty(property2)) {
            System.out.printf("The property [%s] is wrong.\n" +
                    "Available properties: " +
                    "[BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, EVEN, ODD]\n", property2);
        } else {
            if (checkMutuallyExclusiveProperties(property1, property2)) {
                System.out.printf("The request contains mutually exclusive properties: [%s, %s]\n" +
                        "There are no numbers with these properties.\n", property1, property2);
            } else {
                int count = 0;
                while (count != n2) {
                    if (checkStringProperty(n1, property1) && checkStringProperty(n1, property2)) {
                        printPropertiesInLine(n1);
                        count++;
                    }
                    n1++;
                }
            }
        }
        start();
    }

    public static void startForFindNumbersWithManyProperties(long n1, long n2, String[] properties) {


        String[] mutuallyExclusiveProp = checkArrayMutuallyExclusiveProperties(properties);

        List<String> wrongProperties = new ArrayList<>();
        for (String prop : properties) {
            if (!checkInputProperty(prop)) {
                wrongProperties.add(prop);
            }
        }

        if (wrongProperties.size() == 1) {
            System.out.printf("The property [%s] is wrong.\n" +
                    "Available properties: " +
                    "[BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, " +
                    "EVEN, ODD, JUMPING, HAPPY, SAD]\n", wrongProperties.get(0));
        } else if (wrongProperties.size() > 1) {
            System.out.println("The properties " + wrongProperties + " are wrong.\n" +
                    "Available properties: " +
                    "[BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, " +
                    "EVEN, ODD, JUMPING, HAPPY, SAD]");
        } else if (mutuallyExclusiveProp != null) {
            System.out.println("The request contains mutually exclusive properties: " +
                    Arrays.toString(mutuallyExclusiveProp) +
                    "\nThere are no numbers with these properties.");
        } else {
            int count = 0;
            boolean bool;
            while (count != n2) {
                bool = true;
                for (String prop : properties) {
                    if (prop.startsWith("-")) {
                        if (checkStringProperty(n1, prop.substring(1))) {
                            bool = false;
                            break;
                        }
                    } else {
                        if (!checkStringProperty(n1, prop)) {
                            bool = false;
                            break;
                        }
                    }
                }
                if (bool) {
                    printPropertiesInLine(n1);
                    count++;
                }
                n1++;
            }
        }
        start();
    }

    public static void printProperties(long n) {
        System.out.printf("Properties of %d\n", n);
        boolean even = checkEven(n);
        System.out.printf("even: %b\n", even);
        System.out.printf("odd: %b\n", !even);
        System.out.printf("buzz: %b\n", checkBuzz(n));
        System.out.printf("duck: %b\n", checkDuck(n));
        System.out.printf("palindromic: %b\n", checkPalindromic(n));
        System.out.printf("gapful: %b\n", checkGapful(n));
        System.out.printf("spy: %b\n", checkSpy(n));
        System.out.printf("sunny: %b\n", checkSunny(n));
        System.out.printf("square: %b\n", checkSquare(n));
        System.out.printf("jumping: %b\n", checkJumping(n));
        boolean happy = checkHappy(n);
        System.out.printf("happy: %b\n", happy);
        System.out.printf("sad: %b\n", !happy);
    }

    public static void printPropertiesInLine(long n1) {
        System.out.printf("%d is", n1);
        System.out.print(checkEven(n1) ? " even" : " odd");
        System.out.print(checkBuzz(n1) ? " buzz" : "");
        System.out.print(checkDuck(n1) ? " duck" : "");
        System.out.print(checkPalindromic(n1) ? " palindromic" : "");
        System.out.print(checkGapful(n1) ? " gapful" : "");
        System.out.print(checkSpy(n1) ? " spy" : "");
        System.out.print(checkSunny(n1) ? " sunny" : "");
        System.out.print(checkSquare(n1) ? " square" : "");
        System.out.print(checkJumping(n1) ? " jumping" : "");
        System.out.print(checkHappy(n1) ? " happy" : " sad");
        System.out.print("\n");
    }

    public static boolean checkNatural(long n) {
        return n > 0;
    }

    public static boolean checkEven(long n) {
        return n % 2 == 0;
    }

    public static boolean checkBuzz(long n) {
        return n % 7 == 0 || n % 10 == 7;
    }

    public static boolean checkDuck(long n) {
        return Long.toString(n).contains("0");
    }

    public static boolean checkPalindromic(long n) {
        int d1;
        int d2;
        for (int i = 0; i < Long.toString(n).length() / 2; i++) {
            d1 = i;
            d2 = Long.toString(n).length() - 1 - i;
            if (Long.toString(n).charAt(d1) != Long.toString(n).charAt(d2)) {
                return false;
            }
        }
        return true;

    }

    public static boolean checkGapful(long n) {
        int len = String.valueOf(n).length();
        if (len > 2) {
            long firstDigit = n / (long) (Math.pow(10.0, (len - 1)));
            long lastDigit = n % 10;
            return n % (firstDigit * 10 + lastDigit) == 0;
        } else {
            return false;
        }
    }

    public static boolean checkSpy(long n) {
        String st = Long.toString(n);
        int sum = 0;
        int product = 1;

        for (int i = 0; i < st.length(); i++) {
            sum += Character.getNumericValue(st.charAt(i));
            product *= Character.getNumericValue(st.charAt(i));
        }
        return sum == product;
    }

    public static boolean checkSunny(long n) {
        return checkSquare(n + 1);
    }

    public static boolean checkSquare(long n) {
        return Math.sqrt((double) n) % 1 == 0;
    }

    public static boolean checkJumping(long n) {
        String st = Long.toString(n);
        for (int i = 0; i < st.length() - 1; i++) {
            if (Math.abs(Character.getNumericValue(st.charAt(i))
                    - Character.getNumericValue(st.charAt(i + 1))) != 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkHappy(long n) {
        long currSum = 0;
        long n1 = n;

        while (currSum != 1) {
            currSum = 0;
            while (n1 != 0) {
                currSum += (n1 % 10) * (n1 % 10);
                n1 /= 10;
            }
            if (currSum == n1 || currSum == 4) {
                return false;
            }
            n1 = currSum;
        }
        return true;
    }

    public static boolean checkStringProperty(long n, String property) {
        switch (property) {
            case "even":
                return checkEven(n);
            case "odd":
                return !checkEven(n);
            case "buzz":
                return checkBuzz(n);
            case "duck":
                return checkDuck(n);
            case "palindromic":
                return checkPalindromic(n);
            case "gapful":
                return checkGapful(n);
            case "spy":
                return checkSpy(n);
            case "sunny":
                return checkSunny(n);
            case "square":
                return checkSquare(n);
            case "jumping":
                return checkJumping(n);
            case "happy":
                return checkHappy(n);
            case "sad":
                return !checkHappy(n);
            default:
                return false;

        }
    }

    public static boolean checkInputProperty(String property) {
        String[] listOfProperties = {"buzz", "duck", "palindromic", "gapful", "spy",
                "square", "sunny", "even", "odd", "jumping", "happy", "sad",
                "-buzz", "-duck", "-palindromic", "-gapful", "-spy",
                "-square", "-sunny", "-even", "-odd", "-jumping", "-happy", "-sad"};
        return Arrays.asList(listOfProperties).contains(property);
    }

    public static boolean checkMutuallyExclusiveProperties(String property1, String property2) {
        String[] exclusion1 = {"even", "odd"};
        String[] exclusion2 = {"duck", "spy"};
        String[] exclusion3 = {"sunny", "square"};
        return Arrays.asList(exclusion1).contains(property1) && Arrays.asList(exclusion1).contains(property2)
                || Arrays.asList(exclusion2).contains(property1) && Arrays.asList(exclusion2).contains(property2)
                || Arrays.asList(exclusion3).contains(property1) && Arrays.asList(exclusion3).contains(property2);
    }

    public static String[] checkArrayMutuallyExclusiveProperties(String[] properties) {
        String[] exclusion1 = {"even", "odd"};
        String[] exclusion11 = {"-even", "-odd"};
        String[] exclusion2 = {"duck", "spy"};
        String[] exclusion22 = {"-duck", "-spy"};
        String[] exclusion3 = {"sunny", "square"};
        String[] exclusion33 = {"-sunny", "-square"};
        String[] exclusion4 = {"happy", "sad"};
        String[] exclusion44 = {"-happy", "-sad"};

        for (String prop : properties) {
            if (prop.startsWith("-") && Arrays.asList(properties).contains(prop.substring(1))) {
                return new String[]{prop, prop.substring(1)};
            }
        }

        if (Arrays.asList(properties).contains("even") && Arrays.asList(properties).contains("odd")) {
            return exclusion1;
        } else if (Arrays.asList(properties).contains("-even") && Arrays.asList(properties).contains("-odd")) {
            return exclusion11;
        } else if (Arrays.asList(properties).contains("duck") && Arrays.asList(properties).contains("spy")) {
            return exclusion2;
        } else if (Arrays.asList(properties).contains("-duck") && Arrays.asList(properties).contains("-spy")) {
            return exclusion22;
        } else if (Arrays.asList(properties).contains("sunny") && Arrays.asList(properties).contains("square")) {
            return exclusion3;
        } else if (Arrays.asList(properties).contains("happy") && Arrays.asList(properties).contains("sad")) {
            return exclusion4;
        } else if (Arrays.asList(properties).contains("-happy") && Arrays.asList(properties).contains("-sad")) {
            return exclusion44;
        }
        return null;
    }

}
