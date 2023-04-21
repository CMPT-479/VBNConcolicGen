package vbn.solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class InstrumentedRunner {
    public static int runInstrumented(String programName, String[] programArgs) {
        try {
            String[] pbArgs = {
                    "java",
                    "-cp",
                    "sootOutput" + File.pathSeparator + "target/classes",
                    programName,
            };
            String[] pbArgsWithProgramArgs = new String[pbArgs.length + programArgs.length];
            System.arraycopy(pbArgs, 0, pbArgsWithProgramArgs, 0, pbArgs.length);
            System.arraycopy(programArgs, 0, pbArgsWithProgramArgs, pbArgs.length, programArgs.length);
            System.out.println(Arrays.toString(pbArgsWithProgramArgs));
            ProcessBuilder pb = new ProcessBuilder(pbArgsWithProgramArgs);

            pb.redirectErrorStream(true);
            Process p = pb.start();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            p.waitFor(); // wait for the process to finish
            int exitCode = p.exitValue();
            System.out.println("Program exited with code " + exitCode);

            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
