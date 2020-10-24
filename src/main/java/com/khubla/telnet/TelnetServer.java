/*
 * Copyright (C) khubla.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Tom Everett <tom@khubla.com>, 2018
 */
package com.khubla.telnet;

import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import org.slf4j.*;

import com.khubla.telnet.nvt.*;
import com.khubla.telnet.shell.*;

public class TelnetServer implements Runnable {
	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(TelnetServer.class);
	/**
	 * thread pool size
	 */
	private final int threads;
	/**
	 * port
	 */
	private final int port;
	/**
	 * Shell Factory
	 */
	private final ShellFactory shellFactory;
	/**
	 * run flag
	 */
	private final AtomicBoolean running = new AtomicBoolean(true);

	public TelnetServer(int port, int threads, ShellFactory shellFactory) {
		this.port = port;
		this.threads = threads;
		this.shellFactory = shellFactory;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			/*
			 * thread pool
			 */
			final ExecutorService executorService = new ThreadPoolExecutor(threads, threads, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(threads, true),
					new ThreadPoolExecutor.CallerRunsPolicy());
			/*
			 * listener
			 */
			serverSocket = new ServerSocket(port);
			Socket clientSocket = null;
			System.out.println("Telnet server listening on port: " + port);
			logger.info("Telnet server listening on port: " + port);
			while (running.get()) {
				try {
					/*
					 * accept connection
					 */
					clientSocket = serverSocket.accept();
					/*
					 * show
					 */
					logger.info("Accepted connection from: " + clientSocket.getInetAddress().toString());
					/*
					 * NVT
					 */
					final NVT nvt = new NVT(clientSocket);
					nvt.setNvtSpy(shellFactory.getNVTSpy());
					/*
					 * create shell
					 */
					final Shell shell = shellFactory.createShell(nvt);
					/*
					 * submit to pool
					 */
					executorService.submit(shell);
				} catch (final Exception e) {
					clientSocket.close();
					logger.error(e.getMessage(), e);
				}
			}
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != serverSocket) {
				try {
					serverSocket.close();
				} catch (final Exception e) {
					logger.error(e.getMessage(), e);
				}
				serverSocket = null;
			}
		}
	}

	public void shutdown() {
		running.set(false);
	}

	public void start() {
		final Thread thread = new Thread(this);
		thread.start();
	}
}
