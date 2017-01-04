# livelog
LiveLog - Log Viewer for JEE apps using web-fragments

## Motivation
This project exists to make it very simple enable tailing on log files in Java web projects running in a Servlet 3.0 ready container.

## Requirements
* Servlet 3.0 container (tested with Tomcat 8)
* Java 8

## Usage

* Add livelog to your project (sample below if using maven).
```xml
<dependency>
  <groupId>com.dlmorais</groupId>
  <artifactId>livelog</artifactId>
  <version>0.0.8</version>
</dependency>
```
* Configure your web.xml with log folder.
```xml
<env-entry>
  <env-entry-name>livelog-logdir</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>${catalina.home}/logs/</env-entry-value>
</env-entry>
```
* Run your webapp and access its url with ```/livelog/``` at the end.

## Additional configuration

* To secure livelog page with a token
```xml
<env-entry>
  <env-entry-name>livelog-secure-token</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>$up3r.$3cR3t.t0k3n</env-entry-value>
</env-entry>
```

* To define a custom filter that will be applied to file search
```xml
<env-entry>
  <env-entry-name>livelog-defaultfilefilter</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>myappname\..*\.log</env-entry-value>
</env-entry>
```

* To enable an auxiliary page that groups the contents of the log file using a regex.
    * LiveLog will group by all regex groups present. At least one grouping is mandatory.
```xml
<!-- This sample groups each line ignoring the date at the start of line. -->
<env-entry>
  <env-entry-name>livelog-contentregex</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>^[\d]{4}-[\d]{2}-[\d]{2} [\d]{2}:[\d]{2}:[\d]{2} - (.*)$</env-entry-value>
</env-entry>
```

* To enable custom groupings at the log tailer.
    * LiveLog will group and color the lines live when tailing a log file.
    * Multiples custom groupings can be defined by changing the number at the env-entry-name property.
```xml
<env-entry>
  <description>Defines the custom group 1 name</description>
  <env-entry-name>livelog-customgroup-1-name</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>Fatal / Critical</env-entry-value>
</env-entry>
    
<env-entry>
  <description>Defines the custom group 1 color (CSS syntax) [Optional: default "gray"]</description>
  <env-entry-name>livelog-customgroup-1-color</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>red</env-entry-value>
</env-entry>
    
<env-entry>
  <description>Defines the custom group 1 regex</description>
  <env-entry-name>livelog-customgroup-1-regex</env-entry-name>
  <env-entry-type>java.lang.String</env-entry-type>
  <env-entry-value>^.* - .* - (FATAL|CRITICAL) :: .*$</env-entry-value>
</env-entry>
```

## Sample APP
Repository contains a sample app that generates a log file at ```${catalina.home}/logs/``` folder.

## License
Apache 2.0

Feel free to use anywhere and contribute!
