#!/bin/bash

bundle_url=$1
k2hackbot_root_dir=/tmp
default_hackbot_bundle="http://192.168.5.130:8080/job/BUILD_OTHER-K2HackBot-Bundle/226/artifact/K2HackBot.tar.gz"


# Remove the existing K2HackBot Bundle if bundle_url is provided in the argument by the user.
if ! [ "$bundle_url" == "" ]; then
	default_hackbot_bundle=$bundle_url
	echo -e "\n> Removing existing K2HackBot bundle if already present in the $k2hackbot_root_dir directory."
	rm -rf $k2hackbot_root_dir/K2HackBot*
fi

# Install the K2HackBot Bundle
if [ ! -d "${k2hackbot_root_dir}/K2HackBot" ]; then
	echo -e "\n> Installing the K2HackBot bundle"

	cd $k2hackbot_root_dir

	rm -f K2HackBot.tar.gz

	echo "    Downloading K2HackBot bundle using the URL: $default_hackbot_bundle"
	wget_output=$(wget -t 2 -T 30 -O K2HackBot.tar.gz $default_hackbot_bundle > /dev/null 2>&1)

	# wget -t 2 -T 30 -O K2HackBot.tar.gz $default_hackbot_bundle	

	if [[ $? -ne 0 ]]; then
		echo ""
		echo "Network error. K2HackBot bundle cannot be downloaded."
		exit 1
	fi

	echo "    Extracting K2HackBot bundle"
	tar xf K2HackBot.tar.gz

	if [[ $? -ne 0 ]]; then
		echo ""
		echo "Failed to K2HackBot extract the tar file."
		exit 1
	fi

	cd K2HackBot/

	# Setup K2HackBot project
	echo -e "\n> Setting up the K2HackBot"
	bash install.sh

	if [[ $? -ne 0 ]]; then
		echo ""
		echo "Failed to setup the K2HackBot."
		exit 1
	fi

	#Set PATH variable for K2HackBot
	mypath="$k2hackbot_root_dir/K2HackBot/bin"
	export PATH=$mypath:$PATH
	export LC_ALL="en_US.UTF-8"

else
	
	/tmp/K2HackBot/bin/k2hackbot --version > /dev/null 2>&1
	if [[ $? -ne 0 ]]; then
		echo ""
		echo "k2hackbot command not found. Remove the existing K2HackBot from $k2hackbot_root_dir directory and rerun the script."
		exit 1
	fi
	echo "K2HackBot bundle found in $k2hackbot_root_dir directory"
fi
