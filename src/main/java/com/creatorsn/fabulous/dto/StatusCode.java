package com.creatorsn.fabulous.dto;

/**
 * 状态码
 */
public enum StatusCode {

    // 由于前端设定，success第一个字母小写
    success(200),
    Fail(400),
    UserNotFound(40001),
    UserExists(40002),
    UserInputError(40003),
    EmailExists(40004),
    PhoneExists(40005),
    PasswordError(40006),
    EmailNotVerified(40007),
    CodeError(40008),
    UpdateEmailVerifiedError(40009),
    CodeEmpty(40010),
    EmailAlreadyVerified(40011),
    CodeExists(40012),
    RoleExists(40013),
    RoleNotFound(40014),
    ModelExists(40015),
    ModelIdEmpty(40016),
    ModelNotFound(40017),
    ConvertError(40018),
    ChatNotFound(40019),
    ContextError(40020),
    ParentMessageNotFound(40021),
    RepeatSubmit(40022),
    Deny(403),

    PermissionDeny(40300),
    MessageNotFound(40023),
    UserQuestionRequired(40024),
    RequestFail(40025),
    PriceError(40026),
    ModelEncodingNotFound(40027),
    Finished(40028),
    IdNull(40029),
    EmailTemplateExists(40030),
    EmailTemplateNotFound(40031),
    BalanceNotEnough(40032),
    PaySystemClosed(40033),
    GroupNotExists(40034),
    NotebookNotExists(40035),
    NotUpToDate(40036),
    ConfigNotExists(40037),
    DataSourceNotExists(40038),
    DataStructNameRepeated(40039),
    GroupNameRepeated(40040),
    PartitionNotExists(40041),
    ItemNotExists(40042),
    PageNotExists(40043),
    DataTemplateNotFound(40045),
    PathNotFound(40047),
    PathRepeat(40048),
    ContentEmpty(40049);

    final private int value;

    StatusCode(int value) {
        this.value = value;
    }

    public static StatusCode fromValue(int value) {
        for (var type : StatusCode.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot create value from value: " + value + "!");
    }

    public int getValue() {
        return value;
    }
}
