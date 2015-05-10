I've rencently had to (yet again) deal with Quartz Jobs. I don't really like the framework, but it seems that it's the best option for many job scheduling tasks. I've searched a little bit but apparently the only framework that does the job better is called Obsidian, but it has a pricing table for more for running on more than one node.

Now another option is to go with Java's Executors, but if you foresee having at least some degree of complexity in your scheduling and some requirements changes, I'd suggest going with something more reliable and flexible.

Spring scheduling can be pretty powerful, and does the job on number of cases, but again, if you plan on moving to DB job controlling, multiple nodes or need to fine tune your jobs execution, Quartz does have many feature to support it.

Here's a page from Quartz documentation depicting it's latest features.

I've added a base code that can be used to create a job out of the box, having just some boilerplate code for Spring 4 and Quartz.

This link contains all the code for it (using sbt as build tool): https://github.com/eduardohl/quartz-boiler. Code has been documented, so do a clean up if you plan on using it.

The most important part is contained in the Gist below and configures the three beans that represent the Job, the Trigger for it's schedule and the Scheduler itself. See the Quartz documentation for further details.

http://javamoody.blogspot.ca/2015/05/quartz-boilerplate-spring-4.html