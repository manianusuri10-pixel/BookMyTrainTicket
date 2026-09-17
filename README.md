# BookMyTicket - Corrected Java JDBC Project

This package is based on the previous BookMyTicket Swing + JDBC + MySQL project.

## Structure

```text
BookMyTicket/
├── pom.xml
├── database.sql
├── README.md
└── BookMyTrainTicket/
    ├── BookMyTicketApp.java
    ├── BookingManager.java
    ├── DatabaseManager.java
    ├── LoginOperations.java
    ├── PaymentDialog.java
    ├── PaymentManager.java
    ├── RACQueue.java
    ├── Route.java
    ├── RunApp.java
    ├── Seat.java
    ├── SeatAvailabilityManager.java
    ├── Train.java
    ├── TrainManager.java
    ├── User.java
    └── WaitlistManager.java
```

## Important correction

The previous project was missing `PaymentDialog.java`, while `BookMyTicketApp.java` creates `PaymentDialog` during booking. That caused the compiler error:

```text
cannot find symbol: class PaymentDialog
```

`PaymentDialog.java` is included in this corrected package.

## Compile without Maven

From the project root:

```powershell
mkdir out
javac -encoding UTF-8 -d out BookMyTrainTicket\*.java
```

The source files compile successfully with the JDK compiler.

## Run

MySQL Connector/J is required at runtime.

```powershell
java -cp "out;mysql-connector-j-9.4.0.jar" BookMyTrainTicket.RunApp
```

If the connector JAR is inside `lib`:

```powershell
java -cp "out;lib\mysql-connector-j-9.4.0.jar" BookMyTrainTicket.RunApp
```

## Aiven database

The application uses the following environment variables:

```powershell
$env:AIVEN_DB_HOST="your-aiven-host"
$env:AIVEN_DB_PORT="your-aiven-port"
$env:AIVEN_DB_NAME="defaultdb"
$env:AIVEN_DB_USER="avnadmin"
$env:AIVEN_DB_PASSWORD="your-aiven-password"
```

Do not put the real Aiven password into Java source code or GitHub.

## Maven

```powershell
mvn clean compile
```

The supplied `pom.xml` contains MySQL Connector/J 9.4.0 and Java 17 compiler settings.
