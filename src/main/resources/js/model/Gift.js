class Gift {

    constructor(options) {
        this.id = options.id ? options.id : null;
        this.name = options.name ? options.name : null;
        this.link = options.link ? options.link : null;
        this.picture = options.picture ? options.picture : null;
        this.description = options.description ? options.description : null;
        this.status = options.status ? options.status : null;
        this.eventId = options.eventId ? options.eventId : null;
        this.tagId = options.tagId ? options.tagId : null;
    }
}

export default Gift