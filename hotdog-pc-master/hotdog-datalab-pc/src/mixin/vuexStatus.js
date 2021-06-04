import { mapGetters } from "vuex";
export const vuexStatus = {
    computed: {
        ...mapGetters("coinType", ["_coinName"]),
    },
}