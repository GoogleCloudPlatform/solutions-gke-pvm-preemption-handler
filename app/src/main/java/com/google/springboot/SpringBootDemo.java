/* Copyright 2020 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package com.google.springboot;

import java.sql.Timestamp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootDemo {

  private static Boolean isTerminating = false;

  public static Boolean isTerminating() {
    return isTerminating;
  }

  public static void setTerminating(Boolean isTerminating) {
    SpringBootDemo.isTerminating = isTerminating;
  }

  @RequestMapping("/")
  public String home() {
    System.out.println("Received request at: " + new Timestamp(System.currentTimeMillis()));
    return "hello world!";
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootDemo.class, args);
  }

  @RequestMapping("/prestop")
  public void preStopHook() throws InterruptedException {
    System.out.println("PreStop hook called!!");
    setTerminating(true);
    System.out.println("PreStop hook going to sleep for 20s");
    Thread.sleep(20000);
    System.out.println("Exiting PreStop hook");
  }

  @SuppressWarnings("rawtypes")
  @RequestMapping("/health")
  public ResponseEntity health() {
    if (isTerminating()) {
      System.out.println("Health status fail at: " + new Timestamp(System.currentTimeMillis()));
    }
    return new ResponseEntity(!isTerminating() ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE);
  }
}
