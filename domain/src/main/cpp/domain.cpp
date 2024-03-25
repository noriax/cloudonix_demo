//
// Created by Dragan on 24-Mar-24.
//

#include <jni.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <net/if.h>
#include <ifaddrs.h>
#include <android/log.h>
#include <netdb.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_io_cloudonix_domain_app_domain_NativeIPAddressBridge_getIpAddress(JNIEnv *env, jobject thiz) {
    struct ifaddrs *ifaddr, *ifa;
    int family, s;
    char host[NI_MAXHOST];
    jstring result = nullptr;

    if (getifaddrs(&ifaddr) == -1) {
        return nullptr;
    }

    bool foundIpv6Global = false;
    bool foundIpv4Public = false;
    bool foundIpv4 = false;

    // Traverse the list of interfaces
    for (ifa = ifaddr; ifa != NULL; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr == NULL) {
            continue;
        }

        family = ifa->ifa_addr->sa_family;

        // Check if it's an IPv6 address from the global unicast range
        if (family == AF_INET6) {
            struct sockaddr_in6 *addr = (struct sockaddr_in6 *) ifa->ifa_addr;
            if (!(IN6_IS_ADDR_LINKLOCAL(&addr->sin6_addr) || IN6_IS_ADDR_LOOPBACK(&addr->sin6_addr) ||
                  IN6_IS_ADDR_SITELOCAL(&addr->sin6_addr))) {
                inet_ntop(AF_INET6, &addr->sin6_addr, host, NI_MAXHOST);
                result = env->NewStringUTF(host);
                foundIpv6Global = true;
                break;
            }
        }

        // Check if it's an IPv4 address
        if (family == AF_INET) {
            struct sockaddr_in *addr = (struct sockaddr_in *) ifa->ifa_addr;
            if (!inet_ntop(AF_INET, &addr->sin_addr, host, NI_MAXHOST)) {
                continue;
            }

            // Check if it's a public IPv4 address
            uint32_t ip = ntohl(addr->sin_addr.s_addr);
            if (!(ip >> 24 == 10 ||                 // Class A private network
                  (ip >> 20) == (0xAC1 >> 4) ||     // Class B private network
                  (ip >> 16) == (0xC0A8 >> 8) ||   // Class C private network
                  (ip >> 15) == (0x7F >> 1))) {    // Loopback
                result = env->NewStringUTF(host);
                foundIpv4Public = true;
            } else if (!foundIpv4) {
                result = env->NewStringUTF(host);
                foundIpv4 = true;
            }
        }
    }

    freeifaddrs(ifaddr);

    if (foundIpv6Global) {
        return result;
    } else if (foundIpv4Public) {
        return result;
    } else if (foundIpv4) {
        return result;
    } else {
        return nullptr;
    }
}
