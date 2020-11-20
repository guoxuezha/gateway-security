<template>
  <div>
    <editor id="tinymce" v-model="value" :init="init" />
  </div>
</template>

<script>
// 引入基础文件：
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver'
import 'tinymce/icons/default/icons' // 添加图标
import 'tinymce/plugins/image' // 插入上传图片插件
import 'tinymce/plugins/link' // 插入链接
import 'tinymce/plugins/code' // 插入代码
import 'tinymce/plugins/preview' // 插入预览
import axios from 'axios'
import { getToken } from '@/utils/auth'
// 引入路径
import { getKnowledgeUrl } from '@/api/url'
export default {
  name: 'Tinymce',
  components: {
    Editor
  },
  props: {
    curValue: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      init: {
        language_url: '/Tinymce/langs/zh_CN.js', // 语言包的路径
        language: 'zh_CN', // 语言
        skin_url: '/Tinymce/skins/ui/oxide', // skin路径
        height: 500, // 编辑器高度
        branding: false, // 是否禁用“Powered by TinyMCE”
        menubar: false, // 顶部菜单栏显示
        elementpath: false, // 禁用编辑器底部的状态栏
        paste_data_images: true, // 允许粘贴图像
        plugins: ['image', 'link', 'code', 'preview'],
        toolbar: ['formatselect | bold italic | alignleft aligncenter alignright alignjustify |bullist numlist outdent indent | lists image media table | removeformat link | code preview'],
        images_upload_handler: (blobInfo, success, failure) => {
          const formdata = new FormData()
          formdata.set('file', blobInfo.blob())
          // 上传到服务器后要改地址
          // 这个地址是我自己后台上传图片的地址，改成自己后台上传图片的地址http://localhost:8088/api/upload/img/
          const token = getToken()
          axios.post(getKnowledgeUrl() + '/knowledge/upload/img/', formdata, { headers: { 'Authorization': token }}).then(res => {
            // 上传到服务器后要改地址
            // 这个地址是我图片映射路径http://localhost:8088/images/，可以用nginx
            success(getKnowledgeUrl() + '/images/knowledge/' + res.data.message)
          }).catch(res => {
            failure('error')
          })
        }
      },
      value: this.curValue
    }
  },
  watch: {
    // 动态传参
    curValue(newValue) {
      this.value = newValue
    },
    // 读取输入框内的数据
    value(newValue) {
      this.$emit('input', newValue)
    }
  },
  mounted() {
    tinymce.init({})
  }
}
</script>
