# FixPlayerIp
A simple project that I started a while ago, to restore Minecraft player's IPs under a bunch of reverse proxies.

The idea is based on the 2.4 version of TcpShield's [RealIP](https://github.com/TCPShield/RealIP), before they deprecated CIDR based verification ([more info](https://github.com/TCPShield/RealIP/releases/tag/v2.5))

## Features
I made it because I needed to restore the IP addresses behind [hopper-rs](https://github.com/BRA1L0R/hopper-rs) (more info [here](https://github.com/BRA1L0R/hopper-rs#realip)), and I wanted something that just worked.

This plugin also allows to validate the source domain (or at least the declared one), by list or by regex, other than just the reverse proxy IP (like [RealIP](https://github.com/TCPShield/RealIP)).
It also allows to specify a list of IP that can directly connect to the server.

Feel free to contact me or send a push request in case you wanted to specify IP ranges in the console. I didn't add it because I didn't need it.

## Should you use it?
If you're using the actual [TcpShield](https://tcpshield.com/) service, then you should just stick with [RealIP](https://github.com/TCPShield/RealIP).
On the other hand if you're using something like hopper-rs, maybe give it a try.
