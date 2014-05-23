//
//  BAAClient+Visuality.m
//  Markab
//
//  Created by Michele Longhi on 23/05/14.
//  Copyright (c) 2014 Visuality srl. All rights reserved.
//

#import "BAAClient+Visuality.h"

@implementation BAAClient (Visuality)

- (void)grantAccessToCollection:(NSString *)collectionName
                       objectId:(NSString *)objectId
                         toRole:(NSString *)roleName
                     accessType:(NSString *)access
                     completion:(BAAObjectResultBlock)completionBlock
{
    NSString *path = [NSString stringWithFormat:@"%@/%@/%@/role/%@",
                      collectionName,objectId, access, roleName];
    
    [[BAAClient sharedClient] putPath:path
                           parameters:nil
                              success:^(id responseObject)
     {
         
         completionBlock(self, nil);
         
     }
                              failure:^(NSError *error)
     {
         
         if (completionBlock)
         {
             completionBlock(nil, error);
         }
         
     }];
}

- (void)loadDictionaryObjectsFromCollection:(NSString *)collectionName
                                 withParams:(NSDictionary *)parameters
                                 completion:(BAAArrayResultBlock)completionBlock
{
    [self getPath:[NSString stringWithFormat:@"document/%@",collectionName]
       parameters:parameters
          success:^(id responseObject)
     {
         NSArray *objects = responseObject[@"data"];
         NSMutableArray *result = [NSMutableArray array];
         
         for (NSDictionary *d in objects)
         {
             [result addObject:d];
         }
         
         completionBlock(result, nil);
     }
          failure:^(NSError *error)
     {
         completionBlock(nil, error);
     }];
}

@end
