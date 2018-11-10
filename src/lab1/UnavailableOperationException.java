package lab1;

public class UnavailableOperationException extends RuntimeException
{
    UnavailableOperationException(String message)
    {
        super(message);
    }
}
