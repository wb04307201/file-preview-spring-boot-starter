<template>
  <codemirror
      v-model="code"
      placeholder="Code goes here..."
      :style="{ height: '100vh',width:'100vw' }"
      :autofocus="true"
      :indent-with-tab="true"
      :tab-size="2"
      :extensions="extensions"
      :disabled="true"
  />
</template>

<script>
import {defineComponent, shallowRef} from 'vue'
import {Codemirror} from 'vue-codemirror'
import {javascript} from '@codemirror/lang-javascript'
import {java} from '@codemirror/lang-java'
import {json} from '@codemirror/lang-json'
import {cpp} from '@codemirror/lang-cpp'
import {python} from '@codemirror/lang-python'
import {css} from '@codemirror/lang-css'
import {sql} from '@codemirror/lang-sql'
import {rust} from '@codemirror/lang-rust'
import {xml} from '@codemirror/lang-xml'
import {markdown} from '@codemirror/lang-markdown'
import {lezer} from '@codemirror/lang-lezer'
import {wast} from '@codemirror/lang-wast'
import {html} from '@codemirror/lang-html'
import {php} from '@codemirror/lang-php'
import {angular} from '@codemirror/lang-angular'
import {vue} from '@codemirror/lang-vue'
import {oneDark} from '@codemirror/theme-one-dark'

export default defineComponent({
  components: {
    Codemirror
  },
  setup() {
    let extensions
    switch (language) {
      case "javascript":
        extensions = [javascript(), oneDark]
        break;
      case "java":
        extensions = [java(), oneDark]
        break;
      case "json":
        extensions = [json(), oneDark]
        break;
      case "cpp":
        extensions = [cpp(), oneDark]
        break;
      case "python":
        extensions = [python(), oneDark]
        break;
      case "css":
        extensions = [css(), oneDark]
        break;
      case "sql":
        extensions = [sql(), oneDark]
        break;
      case "rust":
        extensions = [rust(), oneDark]
        break;
      case "xml":
        extensions = [xml(), oneDark]
        break;
      case "markdown":
        extensions = [markdown(), oneDark]
        break;
      case "lezer":
        extensions = [lezer(), oneDark]
        break;
      case "wast":
        extensions = [wast(), oneDark]
        break;
      case "html":
        extensions = [html(), oneDark]
        break;
      case "php":
        extensions = [php(), oneDark]
        break;
      case "angular":
        extensions = [angular(), oneDark]
        break;
      case "vue":
        extensions = [vue(), oneDark]
        break;
      default:
        extensions = [oneDark]
    }

    const code = shallowRef(context)

    return {
      code,
      extensions,
    }
  }
})
</script>

<style scoped>
</style>
