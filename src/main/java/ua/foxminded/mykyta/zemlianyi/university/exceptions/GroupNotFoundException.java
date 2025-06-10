package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class GroupNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GroupNotFoundException(Long id) {
        super("Group with ID: " + id + " not found");
    }
}
