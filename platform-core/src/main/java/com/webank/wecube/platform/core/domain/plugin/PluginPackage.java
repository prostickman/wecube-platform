package com.webank.wecube.platform.core.domain.plugin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webank.wecube.platform.core.domain.SystemVariable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "plugin_packages")
public class PluginPackage {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private String version;


    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageDependency> pluginPackageDependencies = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageMenu> pluginPackageMenus = new LinkedHashSet<>();

//    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
//    private Set<PluginPackageEntity> pluginPackageEntities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<SystemVariable> systemVariables = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageAuthority> pluginPackageAuthorities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageRuntimeResourcesDocker> pluginPackageRuntimeResourcesDocker = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageRuntimeResourcesMysql> pluginPackageRuntimeResourcesMysql = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginPackageRuntimeResourcesS3> pluginPackageRuntimeResourcesS3 = new LinkedHashSet<>();

    @OneToMany(mappedBy = "pluginPackage", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<PluginConfig> pluginConfigs = new LinkedHashSet<>();

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "pluginPackage")
    private Set<PluginInstance> pluginInstances = new LinkedHashSet<>();

    public void addPluginConfig(PluginConfig pluginConfig) {
        this.pluginConfigs.add(pluginConfig);
    }

    public void addPluginInstance(PluginInstance pluginInstance) {
        this.pluginInstances.add(pluginInstance);
    }

    public String getDockerImageFile() {
        //TODO
        return "dockerfile.img";
    }

    public PluginPackage() {
    }

    public PluginPackage(Integer id, String name, String version, Set<PluginPackageDependency> pluginPackageDependencies, Set<PluginPackageMenu> pluginPackageMenus, Set<SystemVariable> systemVariables, Set<PluginPackageAuthority> pluginPackageAuthorities, Set<PluginPackageRuntimeResourcesDocker> pluginPackageRuntimeResourcesDocker, Set<PluginPackageRuntimeResourcesMysql> pluginPackageRuntimeResourcesMysql, Set<PluginPackageRuntimeResourcesS3> pluginPackageRuntimeResourcesS3, Set<PluginConfig> pluginConfigs, Set<PluginInstance> pluginInstances) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.pluginPackageDependencies = pluginPackageDependencies;
        this.pluginPackageMenus = pluginPackageMenus;
        this.systemVariables = systemVariables;
        this.pluginPackageAuthorities = pluginPackageAuthorities;
        this.pluginPackageRuntimeResourcesDocker = pluginPackageRuntimeResourcesDocker;
        this.pluginPackageRuntimeResourcesMysql = pluginPackageRuntimeResourcesMysql;
        this.pluginPackageRuntimeResourcesS3 = pluginPackageRuntimeResourcesS3;
        this.pluginConfigs = pluginConfigs;
        this.pluginInstances = pluginInstances;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<PluginPackageDependency> getPluginPackageDependencies() {
        return pluginPackageDependencies;
    }

    public void setPluginPackageDependencies(Set<PluginPackageDependency> pluginPackageDependencies) {
        this.pluginPackageDependencies = pluginPackageDependencies;
    }

    public Set<PluginPackageMenu> getPluginPackageMenus() {
        return pluginPackageMenus;
    }

    public void setPluginPackageMenus(Set<PluginPackageMenu> pluginPackageMenus) {
        this.pluginPackageMenus = pluginPackageMenus;
    }

    public Set<SystemVariable> getSystemVariables() {
        return systemVariables;
    }

    public void setSystemVariables(Set<SystemVariable> systemVariables) {
        this.systemVariables = systemVariables;
    }

    public Set<PluginPackageAuthority> getPluginPackageAuthorities() {
        return pluginPackageAuthorities;
    }

    public void setPluginPackageAuthorities(Set<PluginPackageAuthority> pluginPackageAuthorities) {
        this.pluginPackageAuthorities = pluginPackageAuthorities;
    }

    public Set<PluginPackageRuntimeResourcesDocker> getPluginPackageRuntimeResourcesDocker() {
        return pluginPackageRuntimeResourcesDocker;
    }

    public void setPluginPackageRuntimeResourcesDocker(Set<PluginPackageRuntimeResourcesDocker> pluginPackageRuntimeResourcesDocker) {
        this.pluginPackageRuntimeResourcesDocker = pluginPackageRuntimeResourcesDocker;
    }

    public Set<PluginPackageRuntimeResourcesMysql> getPluginPackageRuntimeResourcesMysql() {
        return pluginPackageRuntimeResourcesMysql;
    }

    public void setPluginPackageRuntimeResourcesMysql(Set<PluginPackageRuntimeResourcesMysql> pluginPackageRuntimeResourcesMysql) {
        this.pluginPackageRuntimeResourcesMysql = pluginPackageRuntimeResourcesMysql;
    }

    public Set<PluginPackageRuntimeResourcesS3> getPluginPackageRuntimeResourcesS3() {
        return pluginPackageRuntimeResourcesS3;
    }

    public void setPluginPackageRuntimeResourcesS3(Set<PluginPackageRuntimeResourcesS3> pluginPackageRuntimeResourcesS3) {
        this.pluginPackageRuntimeResourcesS3 = pluginPackageRuntimeResourcesS3;
    }

    public Set<PluginConfig> getPluginConfigs() {
        return pluginConfigs;
    }

    public void setPluginConfigs(Set<PluginConfig> pluginConfigs) {
        this.pluginConfigs = pluginConfigs;
    }

    public Set<PluginInstance> getPluginInstances() {
        return pluginInstances;
    }

    public void setPluginInstances(Set<PluginInstance> pluginInstances) {
        this.pluginInstances = pluginInstances;
    }
}
