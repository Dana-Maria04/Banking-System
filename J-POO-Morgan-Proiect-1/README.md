#### Copyright © 2024 Caruntu Dana-Maria 321CAa

### Overview:
 This project simulates a banking system designed to handle essential
financial operations, such as account management, fund transfers, and report  
generation. The system enables users to create accounts, perform transactions,  
manage cards, and review detailed financial reports. It also includes support  
for currency conversion.

### Project Structure:
#### Packages:
  The **cmmd** package contains the classes used for each command needed, 
implementing the Command design pattern. It is based on the Command abstract class
that contains the general fields and methods for each command to inherit. It also
contains the execute method that is implemented by each command, and undo method for
further development. Some of the commands are:
  - **_AddAccount_**: creates a new bank account generating a unique IBAN
  - **_CreateCard_**: generates a unique card number, assigning its type 
(regular or one-time)
  - **_Report_**: generates a transaction report for a specific account
  - **_SplitPayment_**: distribution of a specified amount equally among
multiple accounts, verifying sufficient funds in all accounts

  The **userinfo** package contains the classes used for the user information, 
such as:
  - **_Account_**: manages details like IBAN, balance, currency, and associated cards, 
while supporting operations such as payments, balance updates, and account verification
  - **_User_**: stores personal details, associated accounts, and transaction history
  - **_Card_**: represents a bank card, including one-time and frozen states
  - **_ExchangeGraph_** : contains the graph structure used for currency conversion
  - **_Edge_**: represents an edge in the graph, containing the destination as well 
as the conversion rate

**userinfo** also contains the **transactions** package, which includes the classes
used for transaction management, such as:
  - **_AddAccountTransaction_**: models the creation of a new bank account transaction
  - **_ChangeInterestRateTransaction_**: updates the interest rate of a bank account.
  - **_SplitPaymentTransaction_**: a payment is divided equally among multiple accounts
  - **_PayOnlineTransaction_**: captures details such as the amount and the merchant involved

and many more , implemented using a Factory and Singleton design pattern.

#### Classes:

For the command classes, I used `Map<String, Object>` collections to reduce the number of parameters.
This approach allows for flexible data handling, especially when invoking the factory pattern.
The collections store various properties, making the code more modular, easier to maintain, and
simplifying transaction creation.

The `ExchangeGraph` class uses a modified Breadth-First Search (BFS) to find the highest  
conversion rate between currencies. It applies edge relaxation to update the  
best-known rate for each node by exploring all paths. Unlike Dijkstra, it uses  
a FIFO queue and focuses on maximizing conversion rates rather than shortest  
paths.

The `CreateTransaction` class implements both the **Singleton** and **Factory** design patterns.  
The Singleton Pattern ensures that only one instance of the factory exists, providing a consistent  
way to create transactions across the application. The Factory Pattern enables dynamic creation of  
different transaction types using a string identifier (`type`) and parameters map (`params`).  
This design centralizes transaction creation, ensures modularity, and adheres to the  
**Open/Closed Principle**, allowing easy extension for new transaction types.

The `Transaction` class is abstract, serving as a base for all transaction types,  
ensuring shared functionality without direct instantiation. It is inherited by all
tyeps of transactions, overriding addDetailsToNode to write details of each specific transaction.

The `Command` class is abstract, serving as a base for all command types, ensuring  
shared functionality like adding responses to the output or constructing parameters  
for transactions. It provides a structure for command execution through the abstract  
`execute` and `undo` methods, which are overridden by specific command implementations.  
This ensures modularity and code reuse across all commands in the application.

The `CommandHandler` class processes commands in the banking application. It  
takes a list of `CommandInput` objects and executes the appropriate command  
logic using a switch statement. This structure ensures modularity and separates  
the logic for each command. Additionally, it creates the `ExchangeGraph` from  
input data to handle currency conversion tasks within commands.

### OOP Concepts Used:
- Inheritance
- Polymorphism
- Encapsulation
- Collection
- Interfaces
- Design Patterns: Factory, Singleton, Command
- Wrapper Classes (e.g., Integer)
- Packages
- Exception Handling
- Method Overriding
- Final Classes/Methods/Variables
- Lombok

### Resources Used:
- [Refactoring Guru](https://refactoring.guru/)
- [OCW - POO-CA-CD](https://ocw.cs.pub.ro/courses/poo-ca-cd)
