package com.mung.blog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class BlogApplication

fun main(args: Array<String>) {
	runApplication<BlogApplication>(*args)
}

