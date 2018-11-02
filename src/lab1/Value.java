package lab1;

public abstract class Value implements Cloneable
{
    private Object body;

    public abstract Object Get();

    public abstract String toString();
    public abstract Value add(Value v);
    public abstract Value sub(Value v);
    public abstract Value mul(Value v);
    public abstract Value div(Value v);
    public abstract Value pow(Value v);
    public abstract boolean eq(Value v);
    public abstract boolean lte(Value v);
    public abstract boolean lt(Value v);
    public abstract boolean gte(Value v);
    public abstract boolean gt(Value v);
    public abstract boolean neq(Value v);
    public abstract boolean equals(Object other);
    public abstract int hashCode();
    public abstract Value create(String s);

    @Override
    protected Value clone() {
        try {
            return (Value) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(this.getClass().getName() + " nie implementuje Cloneable...");
            return null;
        }
    }
}
