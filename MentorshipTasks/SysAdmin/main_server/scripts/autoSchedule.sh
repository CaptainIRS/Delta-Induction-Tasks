#!/bin/bash

function allot_post() {
	touch /home/$2/post_allotted
	echo $1, $3, $4 >> /home/$2/post_allotted
}

echo "Allotting posts..."
export -f allot_post
xargs -n 4 -a $1 bash -c 'allot_post "$@"' _
echo "Posts allocated!"
