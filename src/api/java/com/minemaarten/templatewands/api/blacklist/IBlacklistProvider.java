package com.minemaarten.templatewands.api.blacklist;

import com.minemaarten.templatewands.api.util.BlockContext;

public interface IBlacklistProvider{
    public boolean shouldBlacklist(BlockContext context);
}
