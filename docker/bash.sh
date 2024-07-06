#!/bin/bash
cd "/d/Coding/Java/Java Servlet" || exit
pwd

# Run stripe.exe login
./stripe.exe login

# Run stripe.exe listen with forwarding
./stripe.exe listen --forward-to localhost:8084/api/payment/webhook