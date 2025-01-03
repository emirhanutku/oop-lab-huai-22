import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class ExceptionHandling extends Exception {
    public ExceptionHandling(String massage) {
        super(massage);
    }

    public static void controlExistenceOfBook(int bookId, HashMap<Integer, Book> registeredBooks,String message) throws ExceptionHandling {
        if (!registeredBooks.containsKey(bookId)) {
            throw new ExceptionHandling(message);
        }
    }

    public static void controlExistenceOfMember(int memberId,HashMap<Integer,Member> registeredMembers) throws ExceptionHandling {
        if (!registeredMembers.containsKey(memberId)){
            throw new ExceptionHandling("You cannot borrow this book!");
        }
    }
    public static void controlBookInLibrary(Book book) throws ExceptionHandling {
        if (book.getClass().getSimpleName().equals("HandWrittenBook")){
            throw new ExceptionHandling("You cannot borrow this book!");
        }
        PrintedBook printedBook=(PrintedBook)book;
        if (!printedBook.bookStatus.equals("L")){
            throw new ExceptionHandling("You cannot borrow this book!");
        }

    }

    public static void controlTotalBorrow(Member member) throws ExceptionHandling {
        if (member.getClass().getSimpleName().equals("Academician")){
            if (member.totalBarrow>=4){
                throw new ExceptionHandling("You have exceeded the borrowing limit!");
            }
        }
        else {
            if (member.totalBarrow>=2){
                throw new ExceptionHandling("You have exceeded the borrowing limit!");
            }
        }
    }

    public static void controlReturnBook(Book book) throws ExceptionHandling {
        if (!book.bookStatus.equals("B")&&!book.bookStatus.equals("R")){
            throw new ExceptionHandling("You cannot return this book!");
        }
    }

    public  static void controlReturnMember(ArrayList<PrintedBook> borrowedBook,ArrayList<String> memberId,String member, Book book, String message) throws ExceptionHandling {
        if ((!borrowedBook.contains(book)||!memberId.contains(member))&&(!book.bookStatus.equals("R"))){
            throw new ExceptionHandling(message);
        }


    }

    public static void controlExtendTime(LocalDate currentTime,PrintedBook book) throws ExceptionHandling {
        if (currentTime.isAfter(book.deadLine)){
            throw new ExceptionHandling("You cannot extend the deadline!");
        }
    }
    public  static void controlTotalExtend(PrintedBook book) throws ExceptionHandling {
        if (book.isExtend){
            throw new ExceptionHandling("You cannot extend the deadline!");
        }
    }
    public static void controlReadLibraryHandwritten(String memberType,String bookType) throws ExceptionHandling {
        if (memberType.equals("S")&&bookType.equals("HandWrittenBook")){
            throw new ExceptionHandling("Students can not read handwritten books!");
        }

    }
    public static void controlReadLibraryExistenceBook(Book book) throws ExceptionHandling {
        if (book.bookStatus.equals("B")){
            throw new ExceptionHandling("You can not read this book!");
        }

    }


}
