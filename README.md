[![CI](https://github.com/teverett/ktelnet/actions/workflows/main.yml/badge.svg)](https://github.com/teverett/ktelnet/actions/workflows/main.yml)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b7b3babc5b38465bad5c9d00a940e002)](https://app.codacy.com/gh/teverett/ktelnet/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

# kTelnet

A Java library which implements an embeddable Telnet server. The server is fully multithreaded and supports numerous Telnet RFC's.

## License

kTelnet is licensed under the BSD terms

## Maven Coordinates

```
<groupId>com.khubla.ktelnet</groupId>
<artifactId>ktelnet</artifactId>
<packaging>jar</packaging>
<version></version>
```

## Tested with

* [GNU Telnet](https://www.gnu.org/software/inetutils/)
* [Putty](https://www.putty.org/)
* [TerraTerm](https://teratermproject.github.io/index-en.html)
* [Mocha Telnet](https://www.mochasoft.dk/)
* [Windows Telnet](https://learn.microsoft.com/en-us/windows-server/administration/windows-commands/telnet)

## Using kTelnet

A simple example using the default Telnet implementation:

<pre>
   private final static int THREADS = 20;
   private final static int PORT = 2121;

   public static void main(String[] args) {
      try {
         /*
          * telnet
          */
         final TelnetServer telnetServer = new TelnetServer(PORT, THREADS, new BasicShellFactoryImpl());
         telnetServer.start();
         /*
          * wait
          */
         Thread.sleep(1000);
         System.out.println("Press any key to exit");
         System.in.read();
         /*
          * shutdown
          */
         telnetServer.shutdown();
      } catch (final Exception e) {
         e.printStackTrace();
         System.exit(0);
      }
</pre>

## Implemented RFC's

* [RFC 727](https://datatracker.ietf.org/doc/html/rfc727) - Logout
* [RFC 856](https://datatracker.ietf.org/doc/html/rfc856) - Binary
* [RFC 857](https://datatracker.ietf.org/doc/html/rfc857) - Echo
* [RFC 858](https://datatracker.ietf.org/doc/html/rfc858) - Suppress Go Ahead
* [RFC 859](https://datatracker.ietf.org/doc/html/rfc859) - Status
* [RFC 860](https://datatracker.ietf.org/doc/html/rfc860) - Mark
* [RFC 885](https://datatracker.ietf.org/doc/html/rfc885) - EOR
* [RFC 1091](https://datatracker.ietf.org/doc/html/rfc1091) - Termtype
* [RFC 1073](https://datatracker.ietf.org/doc/html/rfc1073) - Winsize
* [RFC 1079](https://datatracker.ietf.org/doc/html/rfc1079) - Termspeed

Telnet options are enumerated [here](https://www.iana.org/assignments/telnet-options/telnet-options.xhtml)

## Login

Login is implemented by passing an implementation of `AuthenticationHandler` to the shell constructor. A simple properties file based implementation `PropertiesFileAuthenticationHandlerImpl` is provided.

## Custom shells

Custom shells can be implemented by extending the classes `ShellFactory`, `BasicTelnetCommandRegistryImpl` and `AbstractCommand`. A very simple shell which implements the "quit" command is provided: `BasicShellImpl`

## Logging

kTelnet supports logging at the byte level via `NVTSpy`. A console implementation `ConsoleNVTSpyImpl` and a log file implementation `LoggingNVTSpyImpl` are provided.
