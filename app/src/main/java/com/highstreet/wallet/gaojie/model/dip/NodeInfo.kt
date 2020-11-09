package com.highstreet.wallet.gaojie.model.dip

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class NodeInfoBean(
    val application_version: ApplicationVersion?,
    val node_info: NodeInfo?
)

data class ApplicationVersion(
    val app_version: String?,
    val build_tags: String?,
    val client_name: String?,
    val commit: String?,
    val go: String?,
    val name: String?,
    val server_name: String?,
    val version: String?
)

data class NodeInfo(
    val channels: String?,
    val id: String?,
    val listen_addr: String?,
    val moniker: String?,
    val network: String?,
    val other: Other?,
    val protocol_version: ProtocolVersion?,
    val version: String?
)

data class Other(
    val rpc_address: String?,
    val tx_index: String?
)

data class ProtocolVersion(
    val app: String?,
    val block: String?,
    val p2p: String?
)