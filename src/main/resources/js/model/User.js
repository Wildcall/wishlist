class User {
    constructor(option) {
        this.id = option.id ? option.id : null;
        this.email = option.email ? option.email : null;
        this.name = option.name ? option.name : null;
        this.picture = option.picture ? option.picture : null;
        this.authType = option.authType ? option.authType : null;
        this.role = option.role ? option.role : null;
    }
}

export {User}