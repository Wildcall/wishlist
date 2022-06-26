<template>
  <v-container>
    <nav-bar @tab="activeTab"/>
    <v-tabs-items v-model="tab">
      <v-tab-item value="event">
        <events-list :events="eventStore.getActualEvents"
                     :exp-events="eventStore.getExpEvents"
                     @openEvent="openEvent"
        />
      </v-tab-item>
      <v-tab-item value="gift">
        <gifts-list :gifts="giftStore.getGifts"
        />
      </v-tab-item>
      <v-tab-item value="received">
        <gifts-list :gifts="giftStore.getReceivedGifts"
        />
      </v-tab-item>
    </v-tabs-items>
    <div class="v-btn--group d-flex flex-column">
      <v-btn v-if="activeEvent && tab === 'event'"
             fab
             color="yellow"
             class="mb-4"
      >
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
      <v-btn fab
             color="green"
             @click="openNewDialog"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </div>
    <v-dialog v-model="newEventDialog"
              persistent
    >
      <event-form @close="closeNewEventDialog"
                  @save="saveEvent"
      />
    </v-dialog>
  </v-container>
</template>

<script lang="js">
import NavBar from "./ui/nav-bar.vue";
import EventsList from "./EventsList.vue";
import GiftsList from "./GiftsList.vue";
import Tag from "./Tag.vue";
import EventForm from "./ui/event-form.vue";

import {useTagStore} from "../store/tag";
import {useEventStore} from "../store/event";
import {useGiftStore} from "../store/gift";
import Event from "../model/Event";


export default {
  name: "Main",
  components: {EventForm, NavBar, EventsList, Tag, GiftsList},

  setup() {
    return {
      eventStore: useEventStore(),
      giftStore: useGiftStore(),
      tagStore: useTagStore()
    }
  },

  data() {
    return {
      tab: 'event',
      activeEvent: null,
      newEventDialog: false,
      editEventDialog: false,
    }
  },

  methods: {
    activeTab(tab: string) {
      this.tab = tab
    },

    openNewDialog() {
      if (this.tab === 'event')
        this.openNewEventDialog()
    },

    openEvent(event: Event) {
      this.activeEvent = event
    },

    openNewEventDialog() {
      this.newEventDialog = true
    },

    closeNewEventDialog() {
      this.newEventDialog = false
    },

    openEditEventDialog() {
      this.editEventDialog = true
    },

    closeEditEventDialog() {
      this.editEventDialog = true
    },

    saveEvent(event: Event) {
      this.eventStore.create(event)
    }
  },

  mounted() {
    this.eventStore.getAll()
    this.giftStore.getAll()
    this.tagStore.getAll()
  }
}
</script>

<style scoped>
.v-btn--group {
  bottom: 0;
  right: 0;
  position: fixed;
  margin: 0 16px 16px 0;
  z-index: 10;
}
</style>