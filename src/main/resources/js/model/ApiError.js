class ApiError {

    constructor(options) {
        this.timestamp = options.timestamp ? options.timestamp : null;
        this.message = options.message ? options.message : null;
        this.status = options.status ? options.status : null;
        this.subErrors = [];
        if (options.subErrors) {
            for (const item of options.subErrors) {
                this.subErrors.push(new SubError(item))
            }
        }
    }
}

class SubError {
    constructor(options) {
        this.object = options.object ? options.object : null;
        this.field = options.field ? options.field : null;
        this.rejectedValue = options.rejectedValue ? options.rejectedValue : null;
        this.message = options.message ? options.message : null;
    }
}

export {ApiError, SubError}