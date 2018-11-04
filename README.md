[![Travis](https://travis-ci.org/teverett/ktelnet.svg?branch=master)](https://travis-ci.org/teverett/ktelnet)

# kTelnet

A Java library which inputs an RFC-compliant Telnet server.  The server is fully multithreaded and supports extending the Telnet command set via a "shell" abstraction. Support for alternative terminal types such a vt200 and tn3270 is planned.

# License
kTelnet is licensed under the BSD terms

# Maven Coordinates

```
<groupId>com.khubla.ktelnet</groupId>
<artifactId>ktelnet</artifactId>
<packaging>jar</packaging>
```

# Using kTelnet

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


