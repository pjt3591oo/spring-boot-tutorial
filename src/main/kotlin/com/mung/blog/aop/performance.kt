package com.mung.blog.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect

import org.springframework.stereotype.Component
import org.springframework.util.StopWatch


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Performance

@Aspect
@Component
class LogPerformance {

    // PointCut : 적용할 지점 또는 범위 선택
    @Around("@annotation(Performance)")
    @Throws(Throwable::class)
//    @Around("ㅅ")
    private fun publicTarget(pjp: ProceedingJoinPoint): Any {
        println("성능 측정을 시작합니다.")
        val sw = StopWatch()
        sw.start()

        // 비즈니스 로직 (메인 로직)
        val result = pjp.proceed()
        sw.stop()
        println("성능 측정이 끝났습니다.")
//        println("걸린시간: {} ms", sw.lastTaskTimeMillis)
        return result

    }
}