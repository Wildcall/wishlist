import Gift from "./Gift";

class Event {

    constructor(options) {
        this.id = options?.id ? options.id : null;
        this.name = options?.name ? options.name : null;
        this.description = options?.description ? options.description : null;
        this.date = options?.date ? options.date : null;
        this.giftsSet = null;
        if (options?.giftsSet) {
            this.giftsSet = [];
            for (const item of options.giftsSet) {
                this.giftsSet.push(new Gift(item))
            }
        }
    }
}

export default Event