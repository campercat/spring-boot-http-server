package io.codecrafters.springboothttpserver.domain;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;

@Service
public class SocketServerService {

  private static final int PORT = 4221;
  private ExecutorService executor;

  private final ApplicationArguments applicationArguments;


  @Autowired
  public SocketServerService(ApplicationArguments applicationArguments) {
    this.applicationArguments = applicationArguments;
  }

  @PostConstruct
  public void startServer() {
    executor = Executors.newFixedThreadPool(10);
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      serverSocket.setReuseAddress(true);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        executor.submit(new ClientHandler(applicationArguments.getSourceArgs(), clientSocket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}