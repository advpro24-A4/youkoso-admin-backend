plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.sonarqube") version "5.0.0.4638"
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

jacoco{
	toolVersion = "0.8.12"
}

sonarqube {
	properties {
		property("sonar.exclusions", "resources/db/migration/**")
		property("sonar.projectKey", "advpro24-A4_youkoso-admin-backend")
		property ("sonar.organization", "advpro24-a4")
		property( "sonar.host.url", "https://sonarcloud.io")

	}
}


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	compileOnly("org.projectlombok:lombok")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate6")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy("jacocoTestReport") // Generate the report after tests run
}

tasks.jacocoTestReport{
	dependsOn("test")
	reports {
		xml.required.set(true)
	}
}
