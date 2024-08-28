# dbgrep Tool

## About
dbgrep is a command-line tool for databases that searches for specific column entries and displays the corresponding rows, highlighting the matched column value in red in the console.

## Usage
Create a directory named 'connection_profiles' in the same directory as the jar file.
Then, add a connection profile for your database within that folder.

### Connection Profile
The profile files can have any file ending.
Example connection profiles can be found under /connection_profiles.

For more information use the --help command.

### Build jar

``mvn clean package -DskipTests``

### Run jar

``java -jar dbgrep-1.0.jar --help``

### Test data

To test the tool, you can use the test databases by starting the test environment as described below in
the [docker](#docker) section.

## Development

### Prerequisites:

- Java version: 21
- Maven
- docker or docker desktop installed
- Under Windows: wsl2 with docker or docker desktop installed

### Start test environment

#### Docker

To start test environment execute following command in project root directory:

``docker compose up``

With test data:

``docker compose --profile flyway up``

To delete all containers execute:

``docker compose --profile flyway down``

To restart flyway migration execute the command:

``docker compose --profile flyway restart flyway_mysql flyway_postgres``

#### Docker Desktop

To start test environment execute following command in project root directory while docker desktop is running:

``docker compose --profile flyway up``

Everything else can be done in ui.

### Execute tests

Start the test database as described above without test data.

Use following command to execute unit tests:

``mvn surefire:test``

Use following command to execute integration tests:

``mvn failsafe:integration-test``

### Check code quality

Use following command to check your code quality locally:

``mvn -Pcicdprofile checkstyle:check pmd:check spotbugs:check``

For test coverage:

``mvn -Pcicdprofile install``
(Test coverage can be viewed in target/site/jacoco-it or jacoco-ut/index.html)

For separate execution:

- ``mvn -Pcicdprofile checkstyle:check``
- ``mvn -Pcicdprofile pmd:check``
- ``mvn -Pcicdprofile spotbugs:check``
