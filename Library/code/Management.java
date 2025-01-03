import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class Management {

    static Repository repository = new Repository();
    static ReadInLibrary readInLibrary = new ReadInLibrary();
    static BorrowedBook borrowedBook = new BorrowedBook();

    public void addBook(String[] command) {
        int id = repository.registeredBooks.size() + 1;
        if (command[1].equals("H")) {
            Book handWrittenBook = new HandWrittenBook();
            handWrittenBook.id = id;
            handWrittenBook.type = command[1];
            repository.registeredBooks.put(id, handWrittenBook);
            String stringFormat = String.format("Created new book: Handwritten [id: %s]", id);


            Output.writeFile(stringFormat);
        } else {
            Book printedBook = new PrintedBook();
            printedBook.id = id;
            printedBook.type = command[1];
            repository.registeredBooks.put(id, printedBook);
            String stringFormat = String.format("Created new book: Printed [id: %s]", id);


            Output.writeFile(stringFormat);
        }

    }

    public void addMember(String[] command) {
        int id = repository.registeredMembers.size() + 1;
        if (command[1].equals("S")) {
            Member student = new Student();
            student.id = id;
            student.type = command[1];
            repository.registeredMembers.put(id, student);
            String stringFormat = String.format("Created new member: Student [id: %s]", id);


            Output.writeFile(stringFormat);
        } else {
            Member academician = new Academician();
            academician.id = id;
            academician.type = command[1];
            repository.registeredMembers.put(id, academician);
            String stringFormat = String.format("Created new member: Academic [id: %s]", id);


            Output.writeFile(stringFormat);
        }
    }

    public void borrowBook(String[] command) throws ExceptionHandling {
        ExceptionHandling.controlExistenceOfBook(Integer.parseInt(command[1]), repository.registeredBooks, "You cannot borrow this book!");
        ExceptionHandling.controlExistenceOfMember(Integer.parseInt(command[2]), repository.registeredMembers);
        ExceptionHandling.controlBookInLibrary(repository.registeredBooks.get(Integer.parseInt(command[1])));
        ExceptionHandling.controlTotalBorrow(repository.registeredMembers.get(Integer.parseInt(command[2])));

        LocalDate borrowedDate = stringToLocalTime(command[3]);
        PrintedBook printedBook = (PrintedBook) repository.registeredBooks.get(Integer.parseInt(command[1]));
        printedBook.bookStatus = "B";
        printedBook.borrowedDate = borrowedDate;
        borrowedBook.borrowedBook.add(printedBook);
        borrowedBook.memberId.add(command[2]);

        if (controlTypeOfMember(command[2]).equals("A")) {
            printedBook.deadLine = borrowedDate.plusWeeks(2);
        } else {
            printedBook.deadLine = borrowedDate.plusWeeks(1);
        }


        repository.registeredMembers.get(Integer.parseInt(command[2])).totalBarrow = repository.registeredMembers.get(Integer.parseInt(command[2])).totalBarrow + 1;
        String stringFormat = String.format("The book [%s] was borrowed by member [%s] at %s", command[1], command[2], command[3]);


        Output.writeFile(stringFormat);

    }

    public void returnBook(String[] command) throws ExceptionHandling {
        ExceptionHandling.controlReturnBook(repository.registeredBooks.get(Integer.parseInt(command[1])));
        ExceptionHandling.controlExistenceOfBook(Integer.parseInt(command[1]), repository.registeredBooks, "You cannot return this book!");
        ExceptionHandling.controlReturnMember(borrowedBook.borrowedBook, borrowedBook.memberId, command[2], repository.registeredBooks.get(Integer.parseInt(command[1])), "You cannot return this book!");
        if (repository.registeredBooks.get(Integer.parseInt(command[1])).bookStatus.equals("B")) {
            long fee;
            if (repository.registeredMembers.get(Integer.parseInt(command[2])).type.equals("S")) {
                fee = controlFee(1, command);
            } else {
                fee = controlFee(2, command);
            }
            String stringFormat = String.format("The book [%s] was returned by member [%s] at %s Fee: %s", command[1], command[2], command[3], fee);
            PrintedBook printedBook = (PrintedBook) repository.registeredBooks.get(Integer.parseInt(command[1]));
            Member member=repository.registeredMembers.get(Integer.parseInt(command[2]));
            member.totalBarrow -=1;
            printedBook.bookStatus = "L";
            printedBook.borrowedDate = null;
            printedBook.deadLine = null;

            borrowedBook.borrowedBook.remove(printedBook);
            borrowedBook.memberId.remove(command[2]);

            Output.writeFile(stringFormat);
        } else if (repository.registeredBooks.get(Integer.parseInt(command[1])).bookStatus.equals("R")) {
            Book book = repository.registeredBooks.get(Integer.parseInt(command[1]));
            book.bookStatus = "L";
            book.borrowedDate = null;
            readInLibrary.memberBook.remove(book);
            readInLibrary.memberId.remove(command[2]);
            String stringFormat = String.format("The book [%s] was returned by member [%s] at %s Fee: 0", command[1], command[2], command[3]);

            Output.writeFile(stringFormat);
        }

    }

    public void extendBook(String[] command) throws ExceptionHandling {
        ExceptionHandling.controlExistenceOfBook(Integer.parseInt(command[1]), repository.registeredBooks, "You cannot extend the deadline!");
        ExceptionHandling.controlReturnMember(borrowedBook.borrowedBook, borrowedBook.memberId, command[2], repository.registeredBooks.get(Integer.parseInt(command[1])), "You cannot extend the deadline!");
        PrintedBook book = (PrintedBook) repository.registeredBooks.get(Integer.parseInt(command[1]));
        ExceptionHandling.controlExtendTime(stringToLocalTime(command[3]), book);
        ExceptionHandling.controlTotalExtend(book);

        if (controlTypeOfMember(command[2]).equals("A")) {
            book.deadLine = book.deadLine.plusWeeks(2);
        } else {
            book.deadLine = book.deadLine.plusWeeks(1);
        }
        book.isExtend = true;
        String stringFormat = String.format("The deadline of book [%s] was extended by member [%s] at %s", command[1], command[2], command[3]);
        String stringFormat1 = String.format("New deadline of book [%s] is %s", command[1], book.deadLine);

        Output.writeFile(stringFormat);
        Output.writeFile(stringFormat1);


    }

    public void readLibrary(String[] command) throws ExceptionHandling {
        ExceptionHandling.controlReadLibraryHandwritten(controlTypeOfMember(command[2]), repository.registeredBooks.get(Integer.parseInt(command[1])).getClass().getSimpleName());
        Book book = repository.registeredBooks.get(Integer.parseInt(command[1]));
        ExceptionHandling.controlReadLibraryExistenceBook(book);

        book.bookStatus = "R";
        book.borrowedDate = stringToLocalTime(command[3]);
        readInLibrary.memberId.add(command[2]);
        readInLibrary.memberBook.add(book);
        String stringFormat1 = String.format("The book [%s] was read in library by member [%s] at %s", command[1], command[2], command[3]);

        Output.writeFile(stringFormat1);


    }

    public void getHistory(String[] command) {

        Output.writeFile("History of library:");

        Output.writeFile("");
        writeMember("S", "students", "Student");

        Output.writeFile("");
        writeMember("A", "academics", "Academic");

        Output.writeFile("");
        writeBook("P", "printed", "Printed");

        Output.writeFile("");
        writeBook("H", "handwritten", "Handwritten");

        Output.writeFile("");
        writeBorrowed();

        Output.writeFile("");
        writeReadLibrary();


    }

    private LocalDate stringToLocalTime(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ld = LocalDate.parse(time, dtf); // 2018-03-06
        return ld;
    }

    private long controlFee(int week, String[] command) {
        PrintedBook returnBook = (PrintedBook) repository.registeredBooks.get(Integer.parseInt(command[1]));
        LocalDate deadLine = returnBook.deadLine;
        LocalDate currentTime = stringToLocalTime(command[3]);
        if (currentTime.isAfter(deadLine)) {
            return ChronoUnit.DAYS.between(deadLine, currentTime);
        }

        return 0;
    }

    private String controlTypeOfMember(String member) {
        if (repository.registeredMembers.get(Integer.parseInt(member)).getClass().getSimpleName().equals("Academician")) {
            return "A";
        }
        return "S";
    }

    private void writeMember(String memberTypeChar, String memberTypeName, String memberName) {
        long studentNumber = repository.registeredMembers.values().stream()
                .filter(myClass -> myClass.type.equals(memberTypeChar))
                .count();
        String stringFormat1 = String.format("Number of %s: %s", memberTypeName, studentNumber);

        Output.writeFile(stringFormat1);
        for (Member member : repository.registeredMembers.values()) {
            if (member.type.equals(memberTypeChar)) {
                String stringFormat = String.format("%s [id: %s]", memberName, member.id);

                Output.writeFile(stringFormat);
            }
        }
    }


    private void writeBook(String bookTypeChar, String bookTypeName, String bookName) {
        long BookNumber = repository.registeredBooks.values().stream()
                .filter(myClass -> myClass.type.equals(bookTypeChar))
                .count();
        String stringFormat1 = String.format("Number of %s books: %s", bookTypeName, BookNumber);

        Output.writeFile(stringFormat1);
        for (Book book : repository.registeredBooks.values()) {
            if (book.type.equals(bookTypeChar)) {
                String stringFormat = String.format("%s [id: %s]", bookName, book.id);

                Output.writeFile(stringFormat);
            }
        }
    }

    private void writeBorrowed() {

        Output.writeFile("Number of borrowed books: "  + borrowedBook.borrowedBook.size());
        for (int index = 0; index < borrowedBook.borrowedBook.size(); index++) {
            String memberId = borrowedBook.memberId.get(index);
            PrintedBook book = borrowedBook.borrowedBook.get(index);
            String stringFormat = String.format("The book [%s] was borrowed by member [%s] at %s", book.id, memberId, book.borrowedDate);

            Output.writeFile(stringFormat);
        }
    }

    private void writeReadLibrary() {

        Output.writeFile("Number of books read in library: "+readInLibrary.memberId.size());
        for (int index = 0; index < readInLibrary.memberId.size(); index++) {
            String memberId = readInLibrary.memberId.get(index);
            Book book = readInLibrary.memberBook.get(index);
            String stringFormat = String.format("The book [%s] was read in library by member [%s] at %s", book.id, memberId, book.borrowedDate);

            Output.writeFile(stringFormat);
        }
    }


}
