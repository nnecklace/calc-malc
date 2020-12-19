package calcmalc.performance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

import calcmalc.App;
import calcmalc.structures.List;

/**
 * @hidden
 */
public class PerformanceTest {
    private static void start(Path p) throws IOException {
        App app = new App();
        app.read(p);
    }
    
    private class Perf {
        private List<Long> times = new List<>();
        private String filename;

        public Perf(String filename) {
            this.filename = filename;
        }

        public void addTime(Long time) {
            times.append(time);
        }

        public double getAverage() {
            double s = 0;
            for (int i = 0; i < times.size(); ++i) {
                s += times.get(i);
            }
            return (s / times.size()) / 1_000_000_000;
        }
    }

    /**
     * @hidden
     */
    private static double sqrt(double n) {
        if (n < 0.0) {
            return Double.NaN;
        }

        if (n == 0.0) {
            return 0.0;
        }

        double x = n;
        double root;
        // newtons method
        while (true) {  
            root = 0.5 * (x + n / x);  
            if (abs(root - x) < 1) {
                break;
            }
            x = root;  
        }  
        
        return root;
    }

    /**
     * @hidden
     */
    private static double pow(double n, double e) {
        if (e % 1 != 0) {
            return Double.NaN;
        }

        double result = 1.0;

        if (e == 0.0) {
            return result;
        }

        for (int i = 0; i < abs(e); ++i) {
            result *= n;
        }

        if (e < 0.0) {
            return 1.0 / result;
        } 

        return result;
    }

    /**
     * @hidden
     */
    private static double log(double n) {
        int precision = 1000;
        double ln = 0.0;
        for (int i = 1; i <= precision; i = i + 2) {
            ln += ((1.0 / i) * pow((n - 1) / (n + 1), i));
        }

        return 2 * ln;
    }

    /**
     * @hidden
     * @param n
     * @return
     */
    private static double abs(double n) {
        return n > 0 ? n : -n;
    }

    /**
     * @hidden
     */
    private static void compareMathFunctions() {
        Random r = new Random(1337);
        long time;
        List<Long> sqrtTimeList = new List<>();
        List<Long> mathSqrtTimeList = new List<>();
        List<Long> logTimeList = new List<>();
        List<Long> mathLogTimeList = new List<>();

        System.out.println("Comparing Sqrt");

        for (int i = 1; i <= 100000; ++i) {
            int next = r.nextInt(10000);
            time = System.nanoTime();
            sqrt(next);
            time = System.nanoTime() - time;
            sqrtTimeList.append(time);
            time = System.nanoTime();
            Math.sqrt(next);
            time = System.nanoTime() - time;
            mathSqrtTimeList.append(time);
        }

        double s = 0;
        for (int i = 0; i < sqrtTimeList.size(); ++i) {
            s += sqrtTimeList.get(i);
        }
        double r1 = (s / sqrtTimeList.size()) / 1_000_000_000;
        System.out.println("Custom Sqrt ran on average in " + r1);

        s = 0;
        for (int i = 0; i < mathSqrtTimeList.size(); ++i) {
            s += mathSqrtTimeList.get(i);
        }
        double r2 = (s / mathSqrtTimeList.size()) / 1_000_000_000;
        System.out.println("Math Sqrt ran on average in " + r2);
        System.out.println(r1 > r2);

        System.out.println("Comparing Log");

        for (int i = 1; i <= 100000; ++i) {
            int next = r.nextInt(10000);
            time = System.nanoTime();
            log(next);
            time = System.nanoTime() - time;
            logTimeList.append(time);
            time = System.nanoTime();
            Math.log(next);
            time = System.nanoTime() - time;
            mathLogTimeList.append(time);
        }

        s = 0;
        for (int i = 0; i < logTimeList.size(); ++i) {
            s += logTimeList.get(i);
        }
        double r3 = (s / logTimeList.size()) / 1_000_000_000;
        System.out.println("Custom Log ran on average " + r3);

        s = 0;
        for (int i = 0; i < mathLogTimeList.size(); ++i) {
            s += mathLogTimeList.get(i);
        }
        double r4 = (s / mathLogTimeList.size()) / 1_000_000_000;
        System.out.println("Math Log ran on average " + (s / mathLogTimeList.size()) / 1_000_000_000);
        System.out.println(r3 > r4);
    }

    /**
     * @hidden
     * @param args hidden
     * @throws IOException hidden
     */
    public static void main(String[] args) throws IOException {
        //compareMathFunctions();
        PerformanceTest pt = new PerformanceTest();
        StringBuilder st = new StringBuilder();
        long time;
        File folder = new File("src/inputs/");
        File[] listOfFiles = folder.listFiles();
        Perf[] filesTimes = new Perf[listOfFiles.length];
        for (int i = 1; i <= 30; ++i) {
            for (int k = 0; k < listOfFiles.length; ++k) {
                File f = listOfFiles[k];
                time = System.nanoTime();
                start(f.toPath());          
                time = System.nanoTime() - time;
                if (filesTimes[k] == null) {
                    filesTimes[k] = pt.new Perf(f.getName());
                }
                filesTimes[k].addTime(time);
            }
        }

        for (int j = 0; j < filesTimes.length; ++j) {
            Perf p = filesTimes[j];
            st.append(p.filename + " ran on average " + p.getAverage() + " on runs of " + 10 + "\n");
        }

        System.out.println("Results: \n" + st.toString());
    }
}
