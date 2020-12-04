package calcmalc.performance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import calcmalc.App;
import calcmalc.structures.List;

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
            times.push(time);
        }

        public double getAverage() {
            double s = 0;
            for (int i = 0; i < times.size(); ++i) {
                s += times.get(i);
            }
            return (s / times.size()) / 1_000_000_000;
        }
    }

    public static void main(String[] args) throws IOException {
        PerformanceTest pt = new PerformanceTest();
        StringBuilder st = new StringBuilder();
        long time;
        File folder = new File("src/inputs/");
        File[] listOfFiles = folder.listFiles();
        Perf[] filesTimes = new Perf[listOfFiles.length];
        for (int i = 1; i <= 10; ++i) {
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
