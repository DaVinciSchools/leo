# Building Project Leo

Project Leo is built on Linux using [Apache Maven](https://maven.apache.org/).
The instructions on this page are for the [Ubuntu](https://ubuntu.com/)F
distribution. But, other distributions may work as well.

If you use Windows, you can install an instance of
[Ubuntu 22.04 LTS on Windows](https://www.microsoft.com/store/productId/9PN20MSR04DW)
from the Microsoft Store to work in.

## Checking out Source Code

Project Leo has references to other git repositories (submodules). To do a
full checkout, you need to check out its submodules as well. Run the following
commands in a Terminal to do this:

```shell
# Check out the Project Leo source code and submodules.
git clone --recurse-submodules --shallow-submodules git@github.com:DaVinciSchools/leo.git project_leo
```

This will create a subfolder called ```project_leo``` with the source code in
it.

## Build Dependencies

Building and running Project Leo requires the following dependencies:

* [Apache Maven 3](https://maven.apache.org/)
* [Java 17](https://www.java.com/)
* [Node.js](https://nodejs.org/)
* [Docker](https://www.docker.com/)

The first two of these can be installed in Ubuntu using the following
commands:

```shell
# Install Apache Maven and Java.
sudo apt update
sudo apt install maven openjdk-17-doc openjdk-17-jdk openjdk-17-source
```

### Build Dependencies: Node.js

[Node.js](https://nodejs.org/) in the standard Ubuntu repository is very old.

We have a helper script that will add the official Node.js repository and
install ```nodejs``` for you:
[bin/apt/install-nodejs-ubuntu](https://github.com/DaVinciSchools/leo/blob/main/bin/apt/install-nodejs-ubuntu).

### Build Dependencies: Docker Desktop

If you are on a non-production server, it's easiest to install
[Docker Desktop](https://docs.docker.com/get-docker/).

We have a helper script that will add the official Docker repository and
install Docker Desktop for you:
[bin/apt/install-docker-desktop-ubuntu](https://github.com/DaVinciSchools/leo/blob/main/bin/apt/install-docker-desktop-ubuntu).
Log out and log back in so that your group membership is re-evaluated.

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
> Be sure to log out and back in. Docker will not work until this is done
> (including for Maven).

### Build Dependencies: MySQL (Optional)

I recommend installing a local instance of MySQL for development. That way you won't have to start
from scratch every time you run Project Leo.

#### MySQL in Windows with WSL

If you are using WSL, you need to install MySQL in the WSL environment -- not in Windows.

You also need to expose MySQL from WSL to the Windows host. To do that, edit the MySQL
configuration and allow it to accept connections from computers other than ```localhost```.

Edit the file ```/etc/mysql/mysql.conf.d/mysqld.cnf``` and change, or add, the following line:

```ini
bind-address = 0.0.0.0
```

Or change ```0.0.0.0``` to the IP returned by ```wsl hostname -I``` if you want it to be more
restrictive.

Then restart MySQL.

#### Create a Test User with Privileges

For development, Project Leo requires a MySQL setup of:

* A user named "test" with password "test".
* A database named "leo_test".
* Grant ALL privileges for user "test" on database "leo_test".
* Either:
    * Grant SUPER privileges for user "test" to edit triggers (Uncomment the line starting
      with ```GRANT SUPER``` from the commands below).
    * Manually run the generated SQL in
      [```database/src/main/resources/my-sql-generated/*.sql```](https://github.com/DaVinciSchools/leo/tree/main/database/src/main/resources/my-sql-generated)
      after loading the Project Leo schema, below.

The following SQL configures MySQL for development:

```mysql
CREATE DATABASE leo_test;
CREATE USER 'test'@'%' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON leo_test.* TO 'test'@'%';
# Unfortunately, 'test' needs SUPER privileges to edit triggers.
# GRANT SUPER ON *.* TO 'test'@'%';
FLUSH PRIVILEGES;
```

#### Configure Project Leo to Use MySQL

Copy the following properties into a file named
```${HOME}/project_leo.properties```:

```properties
spring.profiles.active=useExternalDatabase
spring.datasource.url=jdbc:mysql://localhost:3306/leo_test
spring.datasource.username=test
spring.datasource.password=test
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### Load Schema & Test Data

To load the schema and add test data to MySQL. After building the system with ```mvn verify```,
run the following command from the root of the repository folder:

```shell
java -jar database/target/project-leo-database-*.jar --loadSchema
```

To load test data, as well as reset it later, run the following command from the root of the
repository folder:

```shell
java -jar database/target/project-leo-database-*.jar --loadTestData
```

The test data includes the following test accounts, all with the password ```password```:

* ```admin@projectleo.net```
* ```teacher@projectleo.net```
* ```student@projectleo.net```

## External Dependencies

By default, Project Leo creates temporary test instances of external
dependencies. To override these to point to real resources, create an
[external properties file](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files).

Project Leo will read additional properties in
```${HOME}/project_leo.properties``` by default. Additional files can be
included with the
[spring.config.import](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.files.importing)
command line flag or SPRING_CONFIG_IMPORT environment variable.

See [```application.properties```](https://github.com/DaVinciSchools/leo/blob/main/server/src/main/resources/application.properties)
in this repository for all property settings. However, for local development,
you will probably only need to override the following "openai.api.key" property
in ```${HOME}/project_leo.properties```. It is necessary to generate projects.

```properties
# Set to the OpenAI key used for queries. Its value starts with "sk-".
openai.api.key=<Your OpenAI key starting with "sk-".>
```

Properties can also be set using environment variables. For instance,
"openai.api.key" can be set using the environment variable "OPENAI_API_KEY".
Environment variables take precedence over property values.

## Building Project Leo

To build Project Leo go to the ```project_leo``` folder and run the following
command:

```shell
# Build Project Leo using the Apache Maven tool.
mvn package
```

This will do a number of things:

* Build the Java Spring server.
* Install Node.js and NPM for the React web client.
* Build the React web client.
* Embed the React web client in the Spring server.
* Run all tests. **Note: These will pause (for a long time) while waiting for
  docker to start a test database.**
* Format code for a code review (see [CONTRIBUTING](CONTRIBUTING.md)).

## Running & Testing Project Leo

Project Leo can be run in the following ways:

### As a Standalone Spring Server

The standalone Spring server serves the compiled React website from an internal
copy. This is best when working on the Spring server itself. Or for production.

The following command runs the Spring server in this way:

```shell
# Run the Spring server in standalone mode from the root project folder.
java -jar server/target/project-leo-server-*.jar
```

Then, open a browser to http://localhost:8080.

### As Separate Spring and React Servers

Running separate Spring and React servers in parallel allows you to modify the
React content and serve it through the Spring server live. The Spring server
will proxy requests to the React server for http content. It's necessary to run
both because the React website makes calls to the Spring server to retrieve
and set data. This is useful when working on the website.

The following commands run the servers in parallel (Note the ```--react_port```
flag):

```shell
# Run the Spring server from the root project folder.
java -jar server/target/project-leo-server-*.jar --react_port=3000
```

``` shell
# Run the React server from the root project folder.
cd server/src/main/clients/web-browser
BROWSER=none npm start
```

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
> Starting the React server may open a browser to http://localhost:3000. This
> does NOT go through the Spring server and will NOT work. You need to go to
> port 8080 on localhost.

Then, open a browser to http://localhost:8080.

### As a Docker Container

Running the server in a Docker container is most helpful for development
without access to a Linux distribution. Go to the parent directory of the
repository and run the following command in the terminal:

```shell
# Run this in the folder above the root project folder.
docker build -t project_leo -f project_leo\Dockerfile .
```

This will build a Docker image with all the dependencies and configuration
packed together. It will also expose the port `8080` automatically. From
here, run the following command to run the Docker container, making sure
to replace `<OPENAI_API_KEY>` with a valid key:

```shell
# the -e option is to define the environment variable for the API call
# the -p option to is expose the ports from 8080 to 8080
# the project_leo argument is to use the image built earlier
docker run -e OPENAI_API_KEY=<OPENAI_API_KEY> -p 8080:8080 project_leo
```

Then, open a browser to http://localhost:8080.

### Jenkins CI

Jenkins uses the follow plugins that must be installed manually:

* Analysis Model API
* AnsiColor
* Pipeline Maven Integration
* Warnings

[^1]: Oxford English Dictionary
