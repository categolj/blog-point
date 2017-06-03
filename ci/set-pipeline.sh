#!/bin/sh
echo y | fly -t home sp -p blog-blog-point -c pipeline.yml -l ../../credentials.yml
