package woojooin.planit.global.security;

public enum Role {
    USER("USER_ROLE"),
    SEMI_USER("SEMI_USER_ROLE");


    Role(String name){
        this.name = name;
    }
    private final String name;
}
