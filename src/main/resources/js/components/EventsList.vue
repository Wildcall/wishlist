<template>
  <v-expansion-panels class="pa-2"
                      v-model="activeEvent"
  >
    <v-expansion-panel v-for="item in events"
                       :key="item.id"
                       class="mb-1"
    >
      <v-expansion-panel-header hide-actions
      >
        <div class="d-flex flex-column">
          <div class="div--event-header">
            {{ item.name }}
          </div>
          <div class="mt-2 div--event-date">
            {{ item.date }}
          </div>
        </div>
      </v-expansion-panel-header>
      <v-expansion-panel-content>
        <div class="div--event-description">
          {{ item.description }}
        </div>
      </v-expansion-panel-content>
    </v-expansion-panel>
    <v-expansion-panel v-for="item in expEvents"
                       :key="item.id"
                       style="color:darkgray;"
                       class="mb-1"
    >
      <v-expansion-panel-header hide-actions>
        <div class="d-flex flex-column">
          <div class="div--event-header">
            {{ item.name }}
          </div>
          <div class="mt-2 div--event-date">
            {{ item.date }}
          </div>
        </div>
      </v-expansion-panel-header>
      <v-expansion-panel-content>
        <div class="div--event-description">
          {{ item.description }}
        </div>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
import EventCard from "./ui/event-card.vue";

export default {
  name: "EventsList",

  components: {EventCard},

  data() {
    return {
      activeEvent: undefined
    }
  },

  watch: {
    activeEvent: function (newValue, oldValue) {
      if (oldValue === undefined && newValue !== undefined)
        this.$emit('openEvent', this.events[newValue] ? this.events[newValue] : this.expEvents[newValue - this.events.length])
      if (newValue === undefined && oldValue !== undefined)
        this.$emit('openEvent', null)
    }
  },

  props: {
    events: [],
    expEvents: []
  }
}
</script>

<style scoped>
.div--event-header {
  font-family: "Roboto Medium", Roboto, sans-serif;
  font-size: 1.2rem;
}

.div--event-description {
  overflow-wrap: break-word;
}

.div--event-date {
  font-family: "Roboto Medium", Roboto, sans-serif;
  color: darkgray;
  font-size: 0.8rem;
}
</style>