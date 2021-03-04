package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessController {

    private Runtime runtime;

    public ProcessController() {
        runtime = Runtime.getRuntime();
    }

    private boolean isSystem(String name, String operationalSystem) {
        return operationalSystem.equals(name);
    }

    private boolean isWindows(String operationalSystem) {
        return isSystem("Windows 10", operationalSystem);
    }

    private boolean isLinux(String operationalSystem) {
        return isSystem("Linux", operationalSystem);
    }

    private String call(String process) {
        try {
            var runningProcess = runtime.exec(process);
            var stream = runningProcess.getInputStream();
            var reader = new InputStreamReader(stream);
            var buffer = new BufferedReader(reader);
            var line = buffer.readLine();
            var result = new StringBuilder();

            while (line != null) {
                line = buffer.readLine();
                if (line != null) {
                    result.append(line).append('\n');
                }
            }

            stream.close();
            reader.close();
            buffer.close();

            return result.toString();
        } catch (IOException e) {
            System.err.println(
                "Você digitou o comando \"" +
                process +
                "\" de maneira incorreta, ou o Java não possui permissão para executar tal processo."
            );
        }

        return "";
    }

    public void list(String operationalSystem) {
        var list = "";
        if (isWindows(operationalSystem)) list = call("TASKLIST /FO TABLE");
        if (isLinux(operationalSystem)) list = call("ps -ef");

        System.out.println(list);
    }

    public void kill(String operationalSystem, String name) {
        if (isWindows(operationalSystem)) killByNameOnWindows(name);
        if (isLinux(operationalSystem)) killByNameOnLinux(name);
    }

    public void kill(String operationalSystem, int pid) {
        if (isWindows(operationalSystem)) killByPidOnWindows(pid);
        if (isLinux(operationalSystem)) killByPidOnLinux(pid);
    }

    public void killByPidOnLinux(int pid) {
        call("kill -9 " + pid);
    }

    public void killByPidOnWindows(int pid) {
        call("TASKKILL /PID " + pid);
    }

    public void killByNameOnLinux(String name) {
        call("pkill -f " + name);
    }

    public void killByNameOnWindows(String name) {
        call("TASKKILL /IM " + name);
    }
}
