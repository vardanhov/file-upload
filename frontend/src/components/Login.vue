<template>
  <v-container>
    <v-layout row>
      <v-flex
          xs12
          class="text-center"
          mt-5
      >
        <h3>Sign In</h3>
      </v-flex>
      <v-flex
          xs12
          sm6
          offset-sm3
          mt-3
      >
        <form @submit.prevent="onSubmit">
          <v-layout column>
            <v-flex>
              <v-alert
                  v-model="alert"
                  type="error"
                  dismissible
              >
                {{ error }}
              </v-alert>
            </v-flex>
            <v-flex>
              <v-text-field
                  id="username"
                  v-model="username"
                  name="username"
                  label="Username"
                  type="text"
                  required
              />
            </v-flex>
            <v-flex>
              <v-text-field
                  id="password"
                  v-model="password"
                  name="password"
                  label="Password"
                  type="password"
                  required
              />
            </v-flex>
            <v-flex
                class="text-center"
                mt-5
            >
              <v-btn
                  type="submit"
                  :disabled="loading"
                  dark
                  color="#2196f3"
                  autofocus
              >
                Sign In
              </v-btn>
            </v-flex>
          </v-layout>
        </form>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      alert: false,
    };
  },

  methods: {
    async springLogin() {
      let username = this.username;
      let password = this.password;
      console.log(username + ' ' + password)
      const credentials =
          "username=" +
          encodeURIComponent(username) +
          "&password=" +
          encodeURIComponent(password);
      let response = await fetch("/login", {
        method: "POST",
        redirect: "follow",
        headers: {"Content-Type": "application/x-www-form-urlencoded"},
        body: credentials

      });

      if (response.status === 200) {
        this.$router.push('/upload');

      } else {
        console.log('Fel!')
        this.$router.push('/login');
      }
    },
    onSubmit(evt) {
      evt.preventDefault()
      this.springLogin()
    }
  }
}
</script>
