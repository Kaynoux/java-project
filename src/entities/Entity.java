package entities;

// Abstract Class which is used for Movie and Staff
public abstract class Entity
{
    protected final int id;
    protected final String name;

    public Entity(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}
