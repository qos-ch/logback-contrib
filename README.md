## License

This project is licensed under the [Apache 2.0 Open Source License](http://www.apache.org/licenses/LICENSE-2.0).


## Project Documentation 

Is [here](https://github.com/qos-ch/logback-contrib/wiki).


## Release notes 

### 0.1.2
* all jars are now also OSGi bundles,
* [add option to append a line separator after each JSON log message](https://github.com/qos-ch/logback-contrib/pull/2),
* [upgrade to jackson2](https://github.com/qos-ch/logback-contrib/pull/4).


## Maven

If you want to use logback-contrib in your project, here is the corresponding Maven dependency declaration:

```xml
<dependency>
    <groupId>ch.qos.logback.contrib</groupId>
    <artifactId>logback-{CONTRIB SUB PROJECT}</artifactId>
    <version>0.1.1</version>
</dependency>
```

You don't have to configure any Maven repository, logback-contrib is on Maven Central.


## Build Instructions

This project requires Maven 2.2.1 or later to build (3.x works as well).  Run the following to build:

```shell
> mvn install
```


## Release engineering

Following steps must be done for a new release:

```shell
> mvn versions:set -DnewVersion=${VERSION_NUMBER} -DgenerateBackupPoms=false
> git tag ${VERSION_NUMBER}
> git push --tags
> mvn clean deploy -Psonatype-oss-release
> mvn versions:set -DnewVersion=${NEW_VERSION_NUMBER}-SNAPSHOT -DgenerateBackupPoms=false
```

