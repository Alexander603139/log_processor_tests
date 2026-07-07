package io.github.bugrov.log_processor_test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SshTunnelManager {

    private static Process tunnelProcess;

    public static void startTunnel() throws IOException, InterruptedException {
        if (tunnelProcess != null && tunnelProcess.isAlive()) {
            System.out.println("SSH tunnel already running");
            return; // уже запущен
        }

        String keyPath = System.getenv("SSH_PRIVATE_KEY_PATH");
        if (keyPath == null || keyPath.isEmpty()) {
            keyPath = System.getProperty("user.home") + "/.ssh/id_ed25519"; // путь по умолчанию
        }
        System.out.println("Starting SSH tunnel with key: " + keyPath);

        // Проверяем, что ключ существует
        if (!Files.exists(Paths.get(keyPath))) {
            throw new IllegalStateException("SSH private key not found at: " + keyPath);
        }

        ProcessBuilder pb = new ProcessBuilder(
                "ssh",
                "-i", keyPath,
                "-L", "5432:localhost:5432",
                "-L", "27017:localhost:27017",
                "-L", "9092:localhost:9092",
                "-L", "8081:localhost:8081",   // <-- добавить
                "-N",
                "root@186.246.14.209"
        );

        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        tunnelProcess = pb.start();
        // Ждём немного, чтобы туннель успел установиться
        System.out.println("SSH tunnel started, PID: " + tunnelProcess.pid());
        Thread.sleep(5000);
    }

    public static void stopTunnel() {
        if (tunnelProcess != null && tunnelProcess.isAlive()) {
            System.out.println("Stopping SSH tunnel...");
            tunnelProcess.destroy();
        }
    }
}
