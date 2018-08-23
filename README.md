# What is Murdock?

Murdock is a howling mad command line app framework inspired in [Play! web framework](http://www.playframework.org) _though it is a bit different. It is for command line, you know?

> This project is archived for good. It was my first open source project, so I'm going to keep it around.

# Who created Murdock?

My name is [Dario](http://dario.im) ([imdario at GitHub](http://github.com/imdario)) and I will be very happy to recieve your comments, issues and suggestions about Murdock :)

Also, you can get more Murdock announcements following me at Twitter: [@im_dario](http://twitter.com/im_dario).

> Murdock has been built in just nine days, starting at July 9th 2010, so keep this in mind!

# How Murdock is useful? (a.k.a. Features)

- **Don't worry about command line stuff:** just develop your modules (check out some examples at the repo) and get everything out of box.
- **REST/Git like command line arguments:** backed by [Apache Commons CLI project](http://commons.apache.org/cli/)
- **Stateless:** Murdock doesn't assume any session state, even in its console mode
- **Console mode:** for those who feels that Java startup is too long, you can have your own console with Murdock, implemented as a module (yeah, that is, Murdock inside one Murdock's module)
- **Pre and post hooks:** as in [Git](http://git-scm.org/), you can attach your own code to other module's specific actions, before or after them. Of course, without modifying their functionality (maybe it would be a good thing?)
- **Pure Java:** you can implement new functionality in plain Java. Theorically, you should be able to create modules in any JVM language (if the resulting Java class is available in the class under hm.murdock.modules package).
