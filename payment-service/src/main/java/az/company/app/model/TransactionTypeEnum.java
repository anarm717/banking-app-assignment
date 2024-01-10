package az.company.app.model;

public enum TransactionTypeEnum {
    TOP_UP((long)1,"Top-up"),
    PURCHASE((long)2,"Purchase"),
    REFUND((long)3,"Refund");

    final Long id;
    final String name;

    TransactionTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

}
