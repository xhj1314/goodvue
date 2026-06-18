// news.ts — 新闻类型定义

export interface News {
  newsId:   number
  title:    string
  content:  string
  creation: string   // ISO datetime string from backend
}
